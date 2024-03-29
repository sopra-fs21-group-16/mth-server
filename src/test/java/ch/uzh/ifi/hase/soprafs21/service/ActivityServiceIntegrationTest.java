package ch.uzh.ifi.hase.soprafs21.service;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        User testUser = userRepository.findById(201L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());
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
        User testUser = userRepository.findById(201L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());
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
        assertThrows(ResponseStatusException.class, () -> activityService.setSwipingStatus(500L, "databaseToken", SwipeStatus.TRUE));

        // delete the specific User, Activity and SwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void setSwipingStatus_WrongUserID_throwsException() {
        //given
        User testUser = userRepository.findById(201L);
        User wrongUser = userRepository.findById(202L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());
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
        User testUser = userRepository.findById(201L);
        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());
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
        User testUser = userRepository.findById(201L);

        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus);
        //userSwipeStatusRepository.save(userSwipeStatus);
        //userSwipeStatusRepository.flush();
        testActivity.setId(50L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity.setCreationDate(LocalDate.now());

        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        // NOTE: content of both objects is equal, but when comparing the objects themselves, then they are not equal
        assertEquals(tests.get(0).getId(),activityService.getAllActivitiesOfUser(testUser).get(0).getId());
        assertEquals(tests.get(0).getCreationDate(),activityService.getAllActivitiesOfUser(testUser).get(0).getCreationDate());

        assertTrue(activityService.getAllActivitiesOfUser(testUser).contains(tests.get(0)));

        // delete the specific activity and userSwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus);
    }

    @Test
    public void getAllActivitiesWithMatchedUsers_success(){
        //given
        User testUser = userRepository.findById(201L);
        User testUser2 = userRepository.findById(202L);

        Activity testActivity = new Activity();
        testActivity.setCreationDate(LocalDate.now());

        // the expected data
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<>();
        UserSwipeStatus userSwipeStatus1 = new UserSwipeStatus(testUser,SwipeStatus.TRUE);
        UserSwipeStatus userSwipeStatus2 = new UserSwipeStatus(testUser2,SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus1);
        userSwipeStatusList.add(userSwipeStatus2);
        userSwipeStatusRepository.save(userSwipeStatus1);
        userSwipeStatusRepository.save(userSwipeStatus2);
        userSwipeStatusRepository.flush();
        testActivity.setId(65L);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);

        testActivity = activityRepository.save(testActivity);
        activityRepository.flush();

        List<Activity> tests = new ArrayList<>();
        tests.add(testActivity);

        // NOTE: content of both objects is equal, but when comparing the objects themselves, then they are not equal
        assertEquals(tests.get(0).getId(),activityService.getAllActivitiesWithMatchedUsers(testUser).get(0).getId());

        // delete the specific activity and userSwipeStatus
        activityRepository.delete(testActivity);
        userSwipeStatusRepository.delete(userSwipeStatus1);
        userSwipeStatusRepository.delete(userSwipeStatus2);
    }

    @Test
    public void generateActivities_validInputs(){
        //given
        User testUser = userRepository.findById(201L);

        //when
        List<Activity> returnedActivities = activityService.generateActivities(testUser.getId());

        //then
        assertFalse(returnedActivities.isEmpty());

        boolean found;
        for (Activity activity : returnedActivities) {
            found = false;
            for (UserSwipeStatus userSwipeStatus : activity.getUserSwipeStatusList()){
                if(userSwipeStatus.getUser().getId().equals(testUser.getId())){
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            activityRepository.delete(activity);
        }
    }
}