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

    public SchedulingSession createSchedulingSession(Long userId1, Long userId2, String token) {
        SchedulingSession schedulingSession = new SchedulingSession();
        User user = userRepository.findByToken(token);
        if (!user.getId().equals(userId1) && !user.getId().equals(userId2)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not start scheduling Session of other users.");
        }

        List<Activity> activityList = activityRepository.findAll();
        List<Activity> sessionActivityList = new ArrayList<Activity>();
        for (Activity activity : activityList) {
            boolean userId1Found = false;
            boolean userId2Found = false;
            for (UserSwipeStatus userSwipeStatus : activity.getUserSwipeStatusList()) {
                if (userSwipeStatus.getUser().getId().equals(userId1) && userSwipeStatus.getSwipeStatus().equals(SwipeStatus.TRUE)) {
                    userId1Found = true;
                }
                if (userSwipeStatus.getUser().getId().equals(userId2) && userSwipeStatus.getSwipeStatus().equals(SwipeStatus.TRUE)) {
                    userId2Found = true;
                }
            }
            if (userId1Found && userId2Found) {
                sessionActivityList.add(activity);
            }
        }
        if (sessionActivityList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduling session not possible for these two users. No common activities.");
        }
        schedulingSession.setActivityList(sessionActivityList);
        SchedulingSession newSchedulingSession = schedulingSessionRepository.save(schedulingSession);
        schedulingSessionRepository.flush();
        return newSchedulingSession;
    }

    public ScheduledActivity saveScheduledActivity(long sessionId, ScheduledActivity scheduledActivity) {
        SchedulingSession schedulingSession = schedulingSessionRepository.findById(sessionId);
        if (schedulingSession == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduling session not found");
        }
        if (scheduledActivity.getActivity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled activity misses specific activity.");
        }
        if (scheduledActivity.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled activity misses specific date.");
        }
        if (scheduledActivity.getLocation() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduled activity misses specific location.");
        }

        ScheduledActivity newScheduledActivity = scheduledActivityRepository.save(scheduledActivity);
        scheduledActivityRepository.flush();
        schedulingSessionRepository.delete(schedulingSession);
        return newScheduledActivity;
    }

    public SchedulingSession getSchedulingSession(long sessionId, String token) {
        SchedulingSession schedulingSession = schedulingSessionRepository.findById(sessionId);
        if (schedulingSession == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduling session Not Found");
        }
        boolean isPartOfMatch = false;
        List<UserSwipeStatus> userSwipeStatusList = schedulingSession.getActivityList().get(0).getUserSwipeStatusList();

        for (UserSwipeStatus userSwipeStatus : userSwipeStatusList) {
            if (userSwipeStatus.getUser().getToken().equals(token)) {
                isPartOfMatch = true;
                break;
            }
        }
        if (!isPartOfMatch) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not part of the scheduling session");
        }
        return schedulingSession;
    }

    public void updateSchedulingSession(long sessionId, SchedulingSession schedulingSession, String token) {
        SchedulingSession schedulingSessionById = schedulingSessionRepository.findById(sessionId);

        boolean noNewData = true;

        if (schedulingSession.getActivityList() != null) {
            schedulingSessionById.setActivityList(schedulingSession.getActivityList());
            noNewData = false;
        }
        if (schedulingSession.getChosenActivity() != null) {
            schedulingSessionById.setChosenActivity(schedulingSession.getChosenActivity());
            noNewData = false;
        }
        if (schedulingSession.getProposerActivity() != null) {
            schedulingSessionById.setProposerActivity(schedulingSession.getProposerActivity());
            noNewData = false;
        }
        if (schedulingSession.getReceiverActivity() != null) {
            schedulingSessionById.setReceiverActivity(schedulingSession.getReceiverActivity());
            noNewData = false;
        }
        if (schedulingSession.getLocationList() != null) {
            schedulingSessionById.setLocationList(schedulingSession.getLocationList());
            noNewData = false;
        }
        if (schedulingSession.getChosenLocation() != null) {
            schedulingSessionById.setChosenLocation(schedulingSession.getChosenLocation());
            noNewData = false;
        }
        if (schedulingSession.getProposerLocation() != null) {
            schedulingSessionById.setProposerLocation(schedulingSession.getProposerLocation());
            noNewData = false;
        }
        if (schedulingSession.getReceiverLocation() != null) {
            schedulingSessionById.setReceiverLocation(schedulingSession.getReceiverLocation());
            noNewData = false;
        }
        if (schedulingSession.getDateList() != null) {
            schedulingSessionById.setDateList(schedulingSession.getDateList());
            noNewData = false;
        }
        if (schedulingSession.getChosenDate() != null) {
            schedulingSessionById.setChosenDate(schedulingSession.getChosenDate());
            noNewData = false;
        }
        if (schedulingSession.getProposerDate() != null) {
            schedulingSessionById.setProposerDate(schedulingSession.getProposerDate());
            noNewData = false;
        }
        if (schedulingSession.getReceiverDate() != null) {
            schedulingSessionById.setReceiverDate(schedulingSession.getReceiverDate());
            noNewData = false;
        }
        if (noNewData) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No changed data was provided");
        }

        try { // saves the given entity but data is only persisted in the database once flush() is called
            schedulingSessionRepository.save(schedulingSessionById);
            schedulingSessionRepository.flush();
        }
        catch (ConstraintViolationException ex) {
            handleValidationError(ex);
        }
    }

    /**
     * to handle errors that occur when violating database constraints
     *
     * @param ex
     */
    private void handleValidationError(ConstraintViolationException ex) {
        AtomicReference exceptions = new AtomicReference<>();

        exceptions.set("Validation failed: \n ");
        ex.getConstraintViolations().forEach(violation -> {
            exceptions.set(exceptions + violation.getMessage() + "\n ");
        });

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exceptions.toString());
    }
}