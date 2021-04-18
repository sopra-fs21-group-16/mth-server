package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Activity Service.
 */
@Service
@Transactional
public class ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;

    private final UserRepository userRepository;

    @Autowired
    public ActivityService(@Qualifier("activityRepository") ActivityRepository activityRepository, @Qualifier("userRepository") UserRepository userRepository ) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public void setSwipingStatus(Long activityId, String token, SwipeStatus swipeStatus) {
        User user= userRepository.findByToken(token);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be identified");
        }
        Activity activity = activityRepository.findByID(activityId);
        if (activity == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exists");
        }
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        userSwipeStatus.setUser(user);
        userSwipeStatus.setSwipeStatus(swipeStatus);

        List<UserSwipeStatus> userSwipeStatusList = activity.getUserSwipeStatusList();
        userSwipeStatusList.add(userSwipeStatus);
        activity.setUserSwipeStatusList(userSwipeStatusList);
        activityRepository.save(activity);
        activityRepository.flush();
    }
}
