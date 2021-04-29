package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void setSwipingStatus_validInputs_success(){
        User testUser = new User();
        testUser.setId(1L);
        testUser.setToken("testToken");
        Activity testActivity = new Activity();
        testActivity.setId(5L);
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        //when
        Mockito.when(userService.getUserByToken("testToken")).thenReturn(testUser);
        Mockito.when(activityRepository.findById(5L)).thenReturn(testActivity);

        //then
        activityService.setSwipingStatus(5L,"testToken", SwipeStatus.TRUE);

        Mockito.verify(activityRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void setSwipingStatus_UserNotInSwipeStatusList_throwsException(){
        User testUser = new User();
        testUser.setId(1L);
        testUser.setToken("testToken");
        Activity testActivity = new Activity();
        testActivity.setId(5L);
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        User newUser = new User();
        newUser.setId(201L);
        userSwipeStatus.setUser(newUser); //other user in SwipeStatusList
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        //when
        Mockito.when(userService.getUserByToken("testToken")).thenReturn(testUser);
        Mockito.when(activityRepository.findById(5L)).thenReturn(testActivity);

        //then
        assertThrows(ResponseStatusException.class, () -> activityService.setSwipingStatus(5L, "testToken", SwipeStatus.TRUE));
    }

    @Test
    public void getAllActivitiesOfUser_success(){
        //given
        User testUser = new User();
        testUser.setId(1L);
        testUser.setToken("testToken");

        Activity testActivity = new Activity();
        testActivity.setId(5L);
        testActivity.setCreationDate(new Date());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus);

        testActivity.setId(50L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        //when
        Mockito.when(activityService.getAllActivitiesOfUser(testUser)).thenReturn(tests);

        // NOTE: content of both objects is equal, but when comparing the objects themselves, then they are not equal
        assertEquals(tests.get(0).getId(),activityService.getAllActivitiesOfUser(testUser).get(0).getId());
    }

    @Test
    public void getAllActivitiesWithMatchedUsers_success(){
        //given
        User testUser = new User();
        testUser.setId(1L);
        User testUser2 = new User();
        testUser2.setId(2L);

        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus(testUser2,SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus1);
        userSwipeStatusList.add(userSwipeStatus2);

        testActivity.setId(65L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        //when
        Mockito.when(activityService.getAllActivitiesOfUser(testUser)).thenReturn(tests);

        // NOTE: content of both objects is equal, but when comparing the objects themselves, then they are not equal
        assertEquals(tests.get(0).getId(),activityService.getAllActivitiesWithMatchedUsers(testUser).get(0).getId());
    }

    @Test
    public void getAllActivitiesWithMatchedUsers_NoMatches_throwsException(){
        //given
        User testUser = new User();
        testUser.setId(1L);
        User testUser2 = new User();
        testUser2.setId(2L);

        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus(testUser2,SwipeStatus.FALSE); // throw exception since no match
        userSwipeStatusList.add(userSwipeStatus1);
        userSwipeStatusList.add(userSwipeStatus2);

        testActivity.setId(65L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        //when
        Mockito.when(activityService.getAllActivitiesOfUser(testUser)).thenReturn(tests);

        //then
        assertThrows(ResponseStatusException.class, () -> activityService.getAllActivitiesWithMatchedUsers(testUser));
    }
}