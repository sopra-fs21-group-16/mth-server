package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(@Qualifier("activityRepository") ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


}
