package ch.uzh.ifi.hase.soprafs21.service;

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
import java.util.stream.Collectors;

/**
 * Scheduling Service.
 */
@Service
@Transactional
public class SchedulingService {

    private final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    private final SchedulingSessionRepository schedulingSessionRepository;

    private final ScheduledActivityRepository scheduledActivityRepository;

    private final ActivityService activityService;

    private final ActivityRepository activityRepository;

    private final UserRepository userRepository;

    @Autowired
    public SchedulingService(@Qualifier("schedulingSessionRepository") SchedulingSessionRepository schedulingSessionRepository,
                             @Qualifier("scheduledActivityRepository") ScheduledActivityRepository scheduledActivityRepository,
                             @Qualifier("activityService") ActivityService activityService,
                             @Qualifier("activityRepository") ActivityRepository activityRepository,
                             @Qualifier("userRepository") UserRepository userRepository) {
        this.schedulingSessionRepository = schedulingSessionRepository;
        this.scheduledActivityRepository = scheduledActivityRepository;
        this.activityService = activityService;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public SchedulingSession createSchedulingSession(long userId1, long userId2, String offer, String token) {
        SchedulingSession schedulingSession = new SchedulingSession();
        User user = userRepository.findByToken(token);
        User user1 = userRepository.findById(userId1);
        User user2 = userRepository.findById(userId2);
        if (!user.getId().equals(userId1) && !user.getId().equals(userId2)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not start scheduling Session of other users.");
        }

        List<Activity> User1ActivityList = activityService.getAllActivitiesWithMatchedUsers(user1);
        List<Activity> User2ActivityList = activityService.getAllActivitiesWithMatchedUsers(user2);
        List<Activity> sessionActivityList= User1ActivityList.stream().distinct().filter(User2ActivityList::contains).collect(Collectors.toList());
        if (User1ActivityList.isEmpty() || User2ActivityList.isEmpty() || sessionActivityList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scheduling session not possible for these two users. No common activities.");
        }
        schedulingSession.setActivityList(sessionActivityList);
        schedulingSession.setOffer(offer);
        SchedulingSession newSchedulingSession = schedulingSessionRepository.save(schedulingSession);
        schedulingSessionRepository.flush();
        return newSchedulingSession;
    }

    public ScheduledActivity saveScheduledActivity(long sessionId, ScheduledActivity scheduledActivity) {
        SchedulingSession schedulingSession = schedulingSessionRepository.findById(sessionId);
        if (schedulingSession == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheduling session not found");
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
        long activityID = scheduledActivity.getActivity().getId();
        scheduledActivity.setActivity(activityRepository.findById(activityID));

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
        if (schedulingSession.getLocationList() != null) {
            schedulingSessionById.setLocationList(schedulingSession.getLocationList());
            noNewData = false;
        }
        if (schedulingSession.getChosenLocation() != null) {
            schedulingSessionById.setChosenLocation(schedulingSession.getChosenLocation());
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
        if (schedulingSession.getOffer() != null) {
            schedulingSessionById.setOffer(schedulingSession.getOffer());
            noNewData = false;
        }
        if (schedulingSession.getAnswer() != null) {
            schedulingSessionById.setAnswer(schedulingSession.getAnswer());
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

    public void checkIfScheduledSessionExistsWithGivenId(long sessionId){
        if(schedulingSessionRepository.findById(sessionId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Scheduling session with session id " + sessionId + " was not found"));
        }
    }

    public void deleteScheduledSession(long sessionId){
        SchedulingSession schedulingSessionToDelete = schedulingSessionRepository.findById(sessionId);

        schedulingSessionRepository.delete(schedulingSessionToDelete);
        schedulingSessionRepository.flush();
    }

    public List<Long> getSchedulingSessionsOfUser(String token) {
        User user = userRepository.findByToken(token);
        List<Long> sessionIdList = new ArrayList<Long>();
        List<SchedulingSession> schedulingSessions = schedulingSessionRepository.findAll();
        for (SchedulingSession schedulingSession : schedulingSessions){
            Activity activity = schedulingSession.getActivityList().get(0);
            if(activity==null){
                continue;
            }
            if (activity.getUserSwipeStatusList().get(0).getUser().getId().equals(user.getId())||activity.getUserSwipeStatusList().get(1).getUser().getId().equals(user.getId())){
                sessionIdList.add(schedulingSession.getId());
            }
        }
        return sessionIdList;
    }

    public ScheduledActivity getSpecificScheduledActivity(long scheduledActivityId,String token) {
        User user = userRepository.findByToken(token);
        ScheduledActivity scheduledActivity = scheduledActivityRepository.findById(scheduledActivityId);
        if (scheduledActivity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Scheduled Activity with id " + scheduledActivityId + " was not found."));
        }
        if (scheduledActivity.getActivity().getUserSwipeStatusList().get(0).getUser().getId().equals(user.getId()) || scheduledActivity.getActivity().getUserSwipeStatusList().get(1).getUser().getId().equals(user.getId())){
            return scheduledActivity;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("User is not part of the Scheduled Activity with id " + scheduledActivityId + "."));
    }

    public List<ScheduledActivity> getAllScheduledActivities(String token) {
        User user = userRepository.findByToken(token);
        List<ScheduledActivity> scheduledActivities = scheduledActivityRepository.findAll();
        List<ScheduledActivity> finalScheduledActivities = new ArrayList<>();
        for (ScheduledActivity scheduledActivity : scheduledActivities){
            if (scheduledActivity.getActivity().getUserSwipeStatusList().get(0).getUser().getId().equals(user.getId()) || scheduledActivity.getActivity().getUserSwipeStatusList().get(1).getUser().getId().equals(user.getId())){
                finalScheduledActivities.add(scheduledActivity);
            }
        }
        return finalScheduledActivities;
    }
}
