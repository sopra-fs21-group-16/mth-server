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

        // if no potential users exist, then don't generate activities
        if(potentialUsers.isEmpty()) { return new ArrayList<>();}

        List<Activity> activityList = new ArrayList<>();
        List<Activity> persistentActivities = new ArrayList<>(getAllUnmatchedActivities(user)); // all unmatched existing activities (still need to be swiped by user, so don't add these again as duplicates)
        List<Activity> matchedActivities = new ArrayList<>(getAllActivitiesWithMatchedUsers(user)); // all activities that are already matched (don't show again!)

        potentialUsers.sort(Comparator.comparing(potentialUser -> getAmountOfOverlappingUserActivityInterests(user.getActivityInterests(), potentialUser.getActivityInterests())));
        Collections.reverse(potentialUsers);

        int PROFILE_LIMIT = 10;
        int UNSEEN_ACTIVITY_LIMIT = 20;
        int i = 1;
        log.info("Profile Ranking:");
        for(User potentialUser : potentialUsers) {
            log.info("{}. Profile: {} (with {} overlapping interests)",i,potentialUser.getName(),getAmountOfOverlappingUserActivityInterests(user.getActivityInterests(), potentialUser.getActivityInterests()));

            Set<ActivityCategory> overlappingInterests = (Set<ActivityCategory>) user.getUserInterests().getActivityInterests().clone();
            overlappingInterests.retainAll(potentialUser.getUserInterests().getActivityInterests());

            for(ActivityCategory overlappingInterest : overlappingInterests) {
                List<ActivityPreset> activityPresets = activityPresetRepository.findByActivityCategory(overlappingInterest);
                for(ActivityPreset activityPreset : activityPresets) {
                    List<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
                    UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus(user, SwipeStatus.INITIAL);
                    UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus(potentialUser, SwipeStatus.INITIAL);
                    
                    userSwipeStatusList.add(userSwipeStatus1);
                    userSwipeStatusList.add(userSwipeStatus2);

                    Activity newActivity = new Activity(activityPreset, userSwipeStatusList);
                    if(!(persistentActivities.contains(newActivity) || matchedActivities.contains(newActivity))) {
                        activityList.add(newActivity);
                        log.info("generateActivities: generate new activity {}", newActivity.getActivityPreset().getActivityName());
                    } else {
                        log.info("generateActivities: wanted to generate new activity, but was already known: {}", newActivity.getActivityPreset().getActivityName());
                    }
                }
            }

            if(i >= PROFILE_LIMIT) {
                break;
            }

            i++;
        }

        int unseenActivitiesAmount = persistentActivities.size();
        for(Activity activity : activityList) {
            if(unseenActivitiesAmount >= UNSEEN_ACTIVITY_LIMIT) {
                log.info("generateActivities: unseenActivities limit reached ({}). No new activities are being added", unseenActivitiesAmount);
                break;
            }
            log.info("generateActivities: Save activity {} in ActivityRepository", activity.getActivityPreset().getActivityName());
            activityRepository.save(activity);
            activityRepository.flush();
            unseenActivitiesAmount++;
        }

        ArrayList<Activity> returnList = new ArrayList<Activity>(getAllUnmatchedActivities(user));
        Collections.shuffle(returnList);
        return returnList;
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
            /* Current user == User 0 */
            if(activity.getUserSwipeStatusList().get(0).getUser().getId().equals(user.getId())
                    && activity.getUserSwipeStatusList().get(0).getSwipeStatus() == SwipeStatus.INITIAL
                    && activity.getUserSwipeStatusList().get(1).getSwipeStatus() != SwipeStatus.FALSE
            ) {
                allUnmatchedActivities.add(activity); // current user's activity
            }

            /* Current user == User 1 */
            if(activity.getUserSwipeStatusList().get(1).getUser().getId().equals(user.getId())
                    && activity.getUserSwipeStatusList().get(1).getSwipeStatus() == SwipeStatus.INITIAL
                    && activity.getUserSwipeStatusList().get(0).getSwipeStatus() != SwipeStatus.FALSE
            ) {
                allUnmatchedActivities.add(activity); // current user's activity
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

            // if potential user has not completed its user profile creation
            if(potentialUser.getGender() == null){
                continue;
            }

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
            if(getAmountOfOverlappingUserActivityInterests(user.getUserInterests().getActivityInterests(),potentialUser.getUserInterests().getActivityInterests()) == 0) {
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

    /**
     * Helper Function to determine amount of overlapping UserActivityInterests
     * @param set1 of first user
     * @param set2 of second user
     * @return amount of overlapping UserActivityInterests
     */
    public int getAmountOfOverlappingUserActivityInterests(Set<ActivityCategory> set1, Set<ActivityCategory> set2) {
        set1.retainAll(set2);
        return set1.size();
    }

}
