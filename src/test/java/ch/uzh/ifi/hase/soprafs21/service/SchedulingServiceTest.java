package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void createSchedulingSession_validInputs_success() {
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
        schedulingSession.setActivityList(activityList);


        //when
        Mockito.when(userRepository.findByToken("Token")).thenReturn(testUser1);
        Mockito.when(activityRepository.findAll()).thenReturn(activityList);
        Mockito.when(schedulingSessionRepository.save(Mockito.any())).thenReturn(schedulingSession);

        //then
        SchedulingSession cratedSchedulingSession = schedulingService.createSchedulingSession(1L, 2L, "Token");

        Mockito.verify(schedulingSessionRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(cratedSchedulingSession.getActivityList(), activityList);
    }

    @Test
    void createSchedulingSession_invalidUserIDs_throwsException() {
        //given
        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setToken("Token");
        User testUser2 = new User();
        testUser2.setId(3L); // ID is set to 3 but but function createSchedulingSession is invoked with 2
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
        schedulingSession.setActivityList(activityList);


        //when
        Mockito.when(userRepository.findByToken("Token")).thenReturn(testUser1);
        Mockito.when(activityRepository.findAll()).thenReturn(activityList);
        Mockito.when(schedulingSessionRepository.save(Mockito.any())).thenReturn(schedulingSession);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.createSchedulingSession(1L, 2L, "Token"));
    }

    @Test
    void createSchedulingSession_wrongUserToken_throwsException() {
        //given
        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setToken("Token");
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setToken("Token2");
        User wrongUser = new User();
        wrongUser.setId(3L);
        wrongUser.setToken("Token3");
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
        schedulingSession.setActivityList(activityList);


        //when
        Mockito.when(userRepository.findByToken("Token3")).thenReturn(wrongUser);
        Mockito.when(activityRepository.findAll()).thenReturn(activityList);
        Mockito.when(schedulingSessionRepository.save(Mockito.any())).thenReturn(schedulingSession);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.createSchedulingSession(1L, 2L, "Token3"));
    }

    @Test
    void saveScheduledActivity_validInputs_success() {
        //given
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        scheduledActivity.setActivity(testActivity);
        scheduledActivity.setLocation("TestLocation");
        LocalDateTime date = LocalDateTime.now();
        scheduledActivity.setDate(date);

        //when
        Mockito.when(schedulingSessionRepository.findById(Mockito.anyLong())).thenReturn(schedulingSession);
        Mockito.when(scheduledActivityRepository.save(Mockito.any())).thenReturn(scheduledActivity);

        //then
        ScheduledActivity savedScheduledActivity = schedulingService.saveScheduledActivity(1L, scheduledActivity);
        Mockito.verify(schedulingSessionRepository, Mockito.times(1)).delete(Mockito.any());
        assertEquals(savedScheduledActivity.getActivity(), testActivity);
        assertEquals(savedScheduledActivity.getDate(), date);
        assertEquals(savedScheduledActivity.getLocation(), "TestLocation");
    }

    @Test
    void saveScheduledActivity_invalidSessionID_throwsException() {
        //given
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        scheduledActivity.setActivity(testActivity);
        scheduledActivity.setLocation("TestLocation");
        LocalDateTime date = LocalDateTime.now();
        scheduledActivity.setDate(date);

        //when
        Mockito.when(schedulingSessionRepository.findById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(scheduledActivityRepository.save(Mockito.any())).thenReturn(scheduledActivity);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.saveScheduledActivity(1L, scheduledActivity));
    }

    @Test
    void saveScheduledActivity_invalidActivityFromScheduledActivity_throwsException() {
        //given
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        scheduledActivity.setLocation("TestLocation");
        LocalDateTime date = LocalDateTime.now();
        scheduledActivity.setDate(date);

        //when
        Mockito.when(schedulingSessionRepository.findById(Mockito.anyLong())).thenReturn(schedulingSession);
        Mockito.when(scheduledActivityRepository.save(Mockito.any())).thenReturn(scheduledActivity);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.saveScheduledActivity(1L, scheduledActivity));
    }

    @Test
    void saveScheduledActivity_invalidLocationFromScheduledActivity_throwsException() {
        //given
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        scheduledActivity.setActivity(testActivity);
        LocalDateTime date = LocalDateTime.now();
        scheduledActivity.setDate(date);

        //when
        Mockito.when(schedulingSessionRepository.findById(Mockito.anyLong())).thenReturn(schedulingSession);
        Mockito.when(scheduledActivityRepository.save(Mockito.any())).thenReturn(scheduledActivity);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.saveScheduledActivity(1L, scheduledActivity));
    }

    @Test
    void saveScheduledActivity_invalidDateFromScheduledActivity_throwsException() {
        //given
        SchedulingSession schedulingSession = new SchedulingSession();
        schedulingSession.setId(1L);
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        Activity testActivity = new Activity();
        testActivity.setId(11L);
        scheduledActivity.setActivity(testActivity);
        scheduledActivity.setLocation("TestLocation");

        //when
        Mockito.when(schedulingSessionRepository.findById(Mockito.anyLong())).thenReturn(schedulingSession);
        Mockito.when(scheduledActivityRepository.save(Mockito.any())).thenReturn(scheduledActivity);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.saveScheduledActivity(1L, scheduledActivity));
    }

    @Test
    void getSchedulingSession_validInputs_success() {
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
        Mockito.when(schedulingSessionRepository.findById(1L)).thenReturn(schedulingSession);

        //then
        SchedulingSession returnedSchedulingSession = schedulingService.getSchedulingSession(1L, "Token");

        assertEquals(returnedSchedulingSession.getActivityList(), activityList);
    }

    @Test
    void getSchedulingSession_invalidToken_throwsException() {
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
        Mockito.when(schedulingSessionRepository.findById(1L)).thenReturn(schedulingSession);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.getSchedulingSession(1L, "WrongToken"));
    }

    @Test
    void getSchedulingSession_invalidSessionID_throwsException() {
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
        schedulingSession.setId(2L); // other sessionID
        schedulingSession.setActivityList(activityList);

        //when
        Mockito.when(schedulingSessionRepository.findById(1L)).thenReturn(null);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.getSchedulingSession(1L, "Token"));
    }

    @Test
    void updateSchedulingSession_validInputs_success() {
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
        LocalDateTime date = LocalDateTime.now();
        List<String> locationList = new ArrayList<>();
        locationList.add("TestLocation");
        List<LocalDateTime> dateList = new ArrayList<>();
        dateList.add(date);

        SchedulingSession newSchedulingSession = new SchedulingSession();
        newSchedulingSession.setActivityList(activityList);
        newSchedulingSession.setChosenActivity(testActivity);
        newSchedulingSession.setProposerActivity(testActivity);
        newSchedulingSession.setReceiverActivity(testActivity);
        newSchedulingSession.setLocationList(locationList);
        newSchedulingSession.setChosenLocation("TestLocation");
        newSchedulingSession.setProposerLocation("TestLocation");
        newSchedulingSession.setReceiverLocation("TestLocation");
        newSchedulingSession.setDateList(dateList);
        newSchedulingSession.setChosenDate(date);
        newSchedulingSession.setProposerDate(date);
        newSchedulingSession.setReceiverDate(date);

        //when
        Mockito.when(schedulingSessionRepository.findById(1L)).thenReturn(schedulingSession);
        Mockito.when(schedulingSessionRepository.save(Mockito.any())).thenReturn(schedulingSession);

        schedulingService.updateSchedulingSession(1L, newSchedulingSession, "Token");

        //then
        Mockito.verify(schedulingSessionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateSchedulingSession_noChanges_throwsException() {
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
        SchedulingSession newSchedulingSession = new SchedulingSession();

        //when
        Mockito.when(schedulingSessionRepository.findById(1L)).thenReturn(schedulingSession);
        Mockito.when(schedulingSessionRepository.save(Mockito.any())).thenReturn(schedulingSession);

        //then
        assertThrows(ResponseStatusException.class, () -> schedulingService.updateSchedulingSession(1L, newSchedulingSession, "Token"));
    }
}