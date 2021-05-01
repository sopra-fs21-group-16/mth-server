package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.*;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityPresetRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Activity Service.
 */
@Service
@Transactional
public class ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
  
    private final ActivityPresetRepository activityPresetRepository;

    private final UserService userService;

    @Autowired
    public ActivityService(@Qualifier("activityRepository") ActivityRepository activityRepository,
                           @Qualifier("activityPresetRepository") ActivityPresetRepository activityPresetRepository,
                           @Qualifier("userService") UserService userService) {
        this.activityRepository = activityRepository;
        this.activityPresetRepository = activityPresetRepository;
        this.userService = userService;
    }
  
  
    public List<Activity> getActivities(long userId, String token) {
        /* ToDo: In the future: check for existing activities that have not been swiped yet (= INITIAL) & add them to returned list */
        userService.isUserAuthenticated(userId, token);
        return generateActivities(userId);
    }

    public void setSwipingStatus(long activityId, String token, SwipeStatus swipeStatus) {
        User user = userService.getUserByToken(token);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be identified");
        }
        Activity activity = activityRepository.findById(activityId);
        if (activity == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exists");
        }

        boolean found = false;
        List<UserSwipeStatus> userSwipeStatusList = activity.getUserSwipeStatusList();
        for (UserSwipeStatus userSwipeStatus : userSwipeStatusList){
            if (userSwipeStatus.getUser().getId().equals(user.getId())) {
                userSwipeStatus.setSwipeStatus(swipeStatus);
                found = true;
                break;
            }
        }
      
        if (!found){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not part of the match.");
        }
        activity.setUserSwipeStatusList(userSwipeStatusList);
        activityRepository.save(activity);
        activityRepository.flush();
    }

    public List<Activity> generateActivities(long userId) {
        User user = userService.getUserByID(userId);
        List<User> potentialUsers = sievePotentialUsers(user);
        List<Activity> activityList = new ArrayList<>();

        for(User potentialUser : potentialUsers) {
            List<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
            userSwipeStatusList.add(new UserSwipeStatus(user, SwipeStatus.INITIAL));
            userSwipeStatusList.add(new UserSwipeStatus(potentialUser, SwipeStatus.INITIAL));

            Set<ActivityCategory> overlappingInterests = user.getUserInterests().getActivityInterests();
            overlappingInterests.retainAll(potentialUser.getUserInterests().getActivityInterests());

            for(ActivityCategory overlappingInterest : overlappingInterests) {
                List<ActivityPreset> activityPresets = activityPresetRepository.findByActivityCategory(overlappingInterest);
                for(ActivityPreset activityPreset : activityPresets) {
                    activityList.add(new Activity(activityPreset, userSwipeStatusList));
                }
            }
        }

        HashSet<Activity> persistentActivities = new HashSet<Activity>(getAllUnmatchedActivities(user));
        activityList.removeAll(persistentActivities);

        activityRepository.saveAll(activityList);
        //if(activityList.size() > 0) {
        //    log.info("generateActivities: Save activity {} in ActivityRepository", activityList.get(0).getActivityPreset().getActivityName());
            //activityRepository.save(activityList.get(0));
            activityRepository.flush();
        //}

        return new ArrayList<>(getAllUnmatchedActivities(user));
    }

    public List<Activity> getAllActivitiesOfUser(User user){
        return activityRepository.findByUserSwipeStatusList_User(user);
    }

    public List<Activity> getAllActivitiesWithMatchedUsers(User user){
        List<Activity> allActivitiesWithMatchedUsers;
        List<Activity> allActivitiesOfUser = getAllActivitiesOfUser(user);

        // for every activity, if in the UserSwipeStatusList both users have swiped TRUE, then we take the activity
        allActivitiesWithMatchedUsers = allActivitiesOfUser.stream().filter(activity -> activity.getUserSwipeStatusList().get(0).getSwipeStatus() == SwipeStatus.TRUE && activity.getUserSwipeStatusList().get(1).getSwipeStatus() == SwipeStatus.TRUE).collect(Collectors.toList());

        if(allActivitiesWithMatchedUsers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matches found");
        }
        return allActivitiesWithMatchedUsers;
    }

    public Set<Activity> getAllUnmatchedActivities(User user){
        Set<Activity> allUnmatchedActivites = new HashSet<>();

        for(Activity activity : getAllActivitiesOfUser(user)) {
            for(UserSwipeStatus userSwipeStatus : activity.getUserSwipeStatusList()) {
                if (userSwipeStatus.getUser().getId().equals(user.getId()) && userSwipeStatus.getSwipeStatus() == SwipeStatus.INITIAL) {
                    allUnmatchedActivites.add(activity); // current user's activity
                } else if (!userSwipeStatus.getUser().getId().equals(user.getId()) && userSwipeStatus.getSwipeStatus() != SwipeStatus.FALSE) {
                    allUnmatchedActivites.add(activity); // potential user's activity
                }
            }
        }

        return allUnmatchedActivites;
    }

    /**
     * Sieve user base such that potentially interesting partners for user are returned
     * @param user
     * @return
     */
    public List<User> sievePotentialUsers(User user) {
        List<User> potentialUsers = new ArrayList<User>();

        for(User potentialUser : userService.getUsers()) {
            if(potentialUser.getId().equals(user.getId())) {
                continue;
            }

            /* Gender Sieve */
            if(!(user.getUserInterests().getGenderPreference() == GenderPreference.EVERYONE ||
                user.getUserInterests().getGenderPreference().toString().equals(potentialUser.getGender().toString()))) {
                log.info("sievePotentialUsers(): Gender Sieve eliminated potential user: {} vs {}", user.getUserInterests().getGenderPreference().toString(), potentialUser.getGender().toString());
                continue;
            }

            /* Age Sieve */
            if(!(potentialUser.getAge() >= user.getUserInterests().getAgeRange().min
                && potentialUser.getAge() <= user.getUserInterests().getAgeRange().max)) {
                log.info("sievePotentialUsers(): Age Sieve eliminated potential user: {} not in range from {} to {}", potentialUser.getAge(), user.getUserInterests().getAgeRange().min, user.getUserInterests().getAgeRange().max);
                continue; // too old or too young
            }

            /* Interest Sieve (passed with at least one common interest) */
            Set<ActivityCategory> set1 = user.getUserInterests().getActivityInterests();
            Set<ActivityCategory> set2 = potentialUser.getUserInterests().getActivityInterests();
            set1.retainAll(set2);
            if(set1.size() == 0) {
                log.info("sievePotentialUsers(): Interest Sieve eliminated potential user");
                continue; // no overlapping interests
            }

            potentialUsers.add(potentialUser);

        }
        log.info("Tarek Test. Size of Potential Users: {}", potentialUsers.size());
        return potentialUsers;
    }

}