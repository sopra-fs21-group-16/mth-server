package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityPresetRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    public List<Activity> generateActivities(long userId) {
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(userService.getUserByID(userId), SwipeStatus.INITIAL);
        List<UserSwipeStatus> userSwipeStatusList = Collections.singletonList(userSwipeStatus); // ToDo: This list should contain two users

        List<Activity> generatedActivities = new ArrayList<Activity>();
        Random rand = new Random();

        /* Generate 10 random activities */
        for(int i = 0; i < 10; i++) {
            // Get random index within activityPresetRepository size
            long n = rand.nextInt((int)activityPresetRepository.count())+1;
            generatedActivities.add(new Activity(activityPresetRepository.findById(n),userSwipeStatusList));
        }

        return generatedActivities;
    }

}
