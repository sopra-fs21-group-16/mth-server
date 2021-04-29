package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityPresetRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserSwipeStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Test class for the ActivityResource REST resource.
 *
 * @see ActivityService
 */
@WebAppConfiguration
@SpringBootTest
public class ActivityServiceIntegrationTest {

    @Qualifier("activityRepository")
    @Autowired
    private ActivityRepository activityRepository;

    @Qualifier("activityPresetRepository")
    @Autowired
    private ActivityPresetRepository activityPresetRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("userSwipeStatusRepository")
    @Autowired
    private UserSwipeStatusRepository userSwipeStatusRepository;

    @Autowired
    private ActivityService activityService;

    @Test
    public void setSwipingStatus_validInputs_success(){
        //given
        User testUser = userRepository.findById(101L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = activityPresetRepository.findById(1L);
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.INITIAL);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        //when
        activityService.setSwipingStatus(testActivity.getId(), "databaseToken", SwipeStatus.TRUE);

        //then
        assertEquals(SwipeStatus.TRUE, activityRepository.findById(testActivity.getId()).get().getUserSwipeStatusList().get(0).getSwipeStatus());

        // delete the specific User, Activity and SwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void setSwipingStatus_WrongActivityID_throwsException(){
        //given
        User testUser = userRepository.findById(1L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(28L);
        ActivityPreset activityPreset = activityPresetRepository.findById(2L);
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () -> activityService.setSwipingStatus(5L, "databaseToken", SwipeStatus.TRUE));

        // delete the specific User, Activity and SwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void setSwipingStatus_WrongUserID_throwsException() {
        //given
        User testUser = userRepository.findById(1L);
        User wrongUser = userRepository.findById(2L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = activityPresetRepository.findById(3L);
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () -> activityService.setSwipingStatus(5L, "databaseToken2", SwipeStatus.TRUE));

        // delete the specific User, Activity and SwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void setSwipingStatus_TokenNotExists_throwsException(){
        //given
        User testUser = userRepository.findById(1L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = activityPresetRepository.findById(4L);
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () -> activityService.setSwipingStatus(5L, "notExistingToken", SwipeStatus.TRUE));

        // delete the specific User, Activity and SwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void getAllActivitiesOfUser_success(){
        //given
        User testUser = userRepository.findById(1L);

        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        testActivity.setId(50L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        // NOTE: content of both objects is equal, but when comparing the objects themselves, then they are not equal
        assertEquals(tests.get(0).getId(),activityService.getAllActivitiesOfUser(testUser).get(0).getId());

        // delete the specific activity and userSwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

}