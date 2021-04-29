package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.*;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ScheduledActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.SchedulingSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Scheduling Service.
 */
@Service
@Transactional
public class SchedulingService {

    private final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    private final SchedulingSessionRepository schedulingSessionRepository;

    private final ScheduledActivityRepository scheduledActivityRepository;

    private final ActivityRepository activityRepository;

    private final UserRepository userRepository;

    @Autowired
    public SchedulingService(@Qualifier("schedulingSessionRepository") SchedulingSessionRepository schedulingSessionRepository,
                             @Qualifier("scheduledActivityRepository") ScheduledActivityRepository scheduledActivityRepository,
                             @Qualifier("activityRepository") ActivityRepository activityRepository,
                             @Qualifier("userRepository") UserRepository userRepository) {
        this.schedulingSessionRepository = schedulingSessionRepository;
        this.scheduledActivityRepository = scheduledActivityRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public void checkIfScheduledSessionExistsWithGivenId(long sessionId){
        try{
            schedulingSessionRepository.findById(sessionId);
        }
        catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Scheduling session with " + sessionId + " was not found"));
        }
    }

    public void deleteScheduledSession(long sessionId){
        SchedulingSession schedulingSessionToDelete = schedulingSessionRepository.findById(sessionId);

        schedulingSessionRepository.delete(schedulingSessionToDelete);
        schedulingSessionRepository.flush();
    }
}
