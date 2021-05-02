package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ScheduledActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.SchedulingSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.doThrow;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulingServiceTest {
    @Mock
    private ScheduledActivityRepository scheduledActivityRepository;

    @Mock
    private SchedulingSessionRepository schedulingSessionRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SchedulingService schedulingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteScheduledSession_success() {
        //given
        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setToken("Token");
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setToken("Token2");
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        List<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus();
        UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus();
        userSwipeStatus1.setUser(testUser1);
        userSwipeStatus1.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatus2.setUser(testUser2);
        userSwipeStatus2.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus1);
        userSwipeStatusList.add(userSwipeStatus2);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        List<Activity> activityList = new ArrayList<>();
        activityList.add(testActivity);
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        schedulingSession.setActivityList(activityList);

        //when
        schedulingService.deleteScheduledSession(schedulingSession.getId());

        Mockito.verify(schedulingSessionRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    void checkIfScheduledSessionExistsWithGivenId_InvalidId_throwException() {
        //given
        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setToken("Token");
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setToken("Token2");
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        List<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus();
        UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus();
        userSwipeStatus1.setUser(testUser1);
        userSwipeStatus1.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatus2.setUser(testUser2);
        userSwipeStatus2.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus1);
        userSwipeStatusList.add(userSwipeStatus2);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        List<Activity> activityList = new ArrayList<>();
        activityList.add(testActivity);
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        schedulingSession.setActivityList(activityList);

        //when
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Scheduling session with session id " + schedulingSession.getId() + " was not found"))).when(schedulingSessionRepository).findById(Mockito.anyLong());

        assertThrows(ResponseStatusException.class, () -> schedulingService.checkIfScheduledSessionExistsWithGivenId(schedulingSession.getId()));
    }


}