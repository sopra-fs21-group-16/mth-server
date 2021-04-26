package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.repository.ScheduledActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.SchedulingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
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

    @Autowired
    public SchedulingService(@Qualifier("schedulingSessionRepository") SchedulingSessionRepository schedulingSessionRepository, @Qualifier("scheduledActivityRepository") ScheduledActivityRepository scheduledActivityRepository) {
        this.schedulingSessionRepository = schedulingSessionRepository;
        this.scheduledActivityRepository = scheduledActivityRepository;
    }

    public ScheduledActivity saveScheduledActivity(long sessionId, ScheduledActivity scheduledActivity) {
        scheduledActivityRepository.save(scheduledActivity);
        scheduledActivityRepository.flush();
        schedulingSessionRepository.delete(schedulingSessionRepository.findById(sessionId));
        return scheduledActivity;
    }

    public SchedulingSession getSchedulingSession(long sessionId, String token) {

        return schedulingSessionRepository.findById(sessionId);
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
        else if (noNewData) {
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