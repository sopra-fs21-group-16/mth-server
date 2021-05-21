package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityPresetRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserSwipeStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private final UserSwipeStatusRepository userSwipeStatusRepository;

    private final UserService userService;

    @Autowired
    public ActivityService(@Qualifier("activityRepository") ActivityRepository activityRepository,
                           @Qualifier("activityPresetRepository") ActivityPresetRepository activityPresetRepository,
                           @Qualifier("userSwipeStatusRepository") UserSwipeStatusRepository userSwipeStatusRepository,
                           @Qualifier("userService") UserService userService) {
        this.activityRepository = activityRepository;
        this.activityPresetRepository = activityPresetRepository;
        this.userService = userService;
        this.userSwipeStatusRepository = userSwipeStatusRepository;
    }
  
  
    public List<Activity> getActivities(String token) {
        /* ToDo: In the future: check for existing activities that have not been swiped yet (= INITIAL) & add them to returned list */
        userService.checkIfValidToken(token);
        return generateActivities(userService.getIdByToken(token));
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
                userSwipeStatusRepository.save(userSwipeStatus);
                userSwipeStatusRepository.flush();
                found = true;
                break;
            }
        }
      
        if (!found){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not part of the match.");
        }
        /* Not necessary because Id of UserSwipeStatusList should stay the same.
        activity.setUserSwipeStatusList(userSwipeStatusList);
        activityRepository.save(activity);
        activityRepository.flush();
        */
    }

    public List<Activity> generateActivities(long userId) {
        User user = userService.getUserByID(userId);
        List<User> potentialUsers = sievePotentialUsers(user);
        List<Activity> activityList = new ArrayList<>();

        for(User potentialUser : potentialUsers) {

            Set<ActivityCategory> overlappingInterests = (Set<ActivityCategory>) user.getUserInterests().getActivityInterests().clone();
            overlappingInterests.retainAll(potentialUser.getUserInterests().getActivityInterests());

            for(ActivityCategory overlappingInterest : overlappingInterests) {
                List<ActivityPreset> activityPresets = activityPresetRepository.findByActivityCategory(overlappingInterest);
                for(ActivityPreset activityPreset : activityPresets) {
                    List<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
                    UserSwipeStatus userSwipeStatus1 = userSwipeStatusRepository.save(new UserSwipeStatus(user, SwipeStatus.INITIAL));
                    UserSwipeStatus userSwipeStatus2 = userSwipeStatusRepository.save(new UserSwipeStatus(potentialUser, SwipeStatus.INITIAL));
                    userSwipeStatusRepository.flush();

                    userSwipeStatusList.add(userSwipeStatus1);
                    userSwipeStatusList.add(userSwipeStatus2);

                    activityList.add(new Activity(activityPreset, userSwipeStatusList));
                }
            }
        }

        List<Activity> persistentActivities = new ArrayList<>(getAllUnmatchedActivities(user)); // all unmatched existing activities
        persistentActivities.addAll(getAllActivitiesWithMatchedUsers(user)); // all matched activities
        log.info("generateActivities: activityList size before dup-delete: {}", activityList.size());
        activityList.removeAll(persistentActivities);
        log.info("generateActivities: activityList size after dup-delete: {}", activityList.size());

        //activityRepository.saveAll(activityList);
        for(Activity activity : activityList) {
            log.info("generateActivities: Save activity {} in ActivityRepository", activity.getActivityPreset().getActivityName());
            activityRepository.save(activity);
            activityRepository.flush();
        }

        return new ArrayList<>(getAllUnmatchedActivities(user));
    }

    public List<Activity> getAllActivitiesOfUser(User user){
        return activityRepository.findByUserSwipeStatusList_User(user);
    }

    public List<Activity> getAllActivitiesWithMatchedUsers(User user){
        List<Activity> allActivitiesWithMatchedUsers;
        List<Activity> allActivitiesOfUser = this.getAllActivitiesOfUser(user);

        // for every activity, if in the UserSwipeStatusList both users have swiped TRUE, then we take the activity
        allActivitiesWithMatchedUsers = allActivitiesOfUser.stream().filter(activity -> activity.getUserSwipeStatusList().get(0).getSwipeStatus() == SwipeStatus.TRUE && activity.getUserSwipeStatusList().get(1).getSwipeStatus() == SwipeStatus.TRUE).collect(Collectors.toList());

        return allActivitiesWithMatchedUsers;
    }

    public Set<Activity> getAllUnmatchedActivities(User user){
        Set<Activity> allUnmatchedActivities = new HashSet<>();

        for(Activity activity : getAllActivitiesOfUser(user)) {
            for(UserSwipeStatus userSwipeStatus : activity.getUserSwipeStatusList()) {
                if (userSwipeStatus.getUser().getId().equals(user.getId()) && userSwipeStatus.getSwipeStatus() == SwipeStatus.INITIAL) {
                    allUnmatchedActivities.add(activity); // current user's activity
                } else if (!userSwipeStatus.getUser().getId().equals(user.getId()) && userSwipeStatus.getSwipeStatus() != SwipeStatus.FALSE) {
                    allUnmatchedActivities.add(activity); // potential user's activity
                }
            }
        }

        return allUnmatchedActivities;
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
            if(!matchingGenderPreferences(user, potentialUser) || !matchingGenderPreferences(potentialUser, user)) {
                log.info("sievePotentialUsers(): Gender Sieve eliminated potential user: {} vs {}", user.getUserInterests().getGenderPreference().toString(), potentialUser.getGender().toString());
                continue;
            }

            /* Age Sieve */
            if(!matchingAgePreferences(user, potentialUser) || !matchingAgePreferences(potentialUser, user)) {
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

    /**
     * Returns true if gender preferences of user match with potentialUser (not symmetric)
     * @param user
     * @param potentialUser
     * @return true if gender preferences of user match with potentialUser
     */
    public boolean matchingGenderPreferences(User user, User potentialUser) {
        return user.getUserInterests().getGenderPreference() == GenderPreference.EVERYONE || user.getUserInterests().getGenderPreference().toString().equals(potentialUser.getGender().toString());
    }

    /**
     * Returns true if potentialUser is in age span of user (not symmetric)
     * @param user
     * @param potentialUser
     * @return true if potentialUser is in age span of user
     */
    public boolean matchingAgePreferences(User user, User potentialUser) {
        return potentialUser.getAge() >= user.getUserInterests().getAgeRange().min
                && potentialUser.getAge() <= user.getUserInterests().getAgeRange().max;
    }

}
