package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.repository.ActivityPresetRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserSwipeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

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
/*
    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        userSwipeStatusRepository.deleteAll();
        activityRepository.deleteAll();
        activityPresetRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        activityRepository.deleteAll();
        userSwipeStatusRepository.deleteAll();
        userRepository.deleteAll();
        activityPresetRepository.deleteAll();
    }

    @Test
    public void setSwipingStatus_validInputs_success(){
        //given
        User testUser = new User();
        testUser.setEmail("test.usertest@uzh.ch");
        testUser.setToken("testToken1");
        testUser.setName("newTester");
        testUser.setPassword("testPassword");
        testUser.setLastSeen(LocalDateTime.now());
        userRepository.save(testUser);
        userRepository.flush();
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = new ActivityPreset();
        activityPreset.setActivityName("test");
        activityPreset.setActivityCategory(ActivityCategory.MUSEUMS);
        activityPreset.setGooglePOICategory("test");
        activityPreset.setGooglePOIKeyword("test");
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity= activityRepository.save(testActivity);
        activityRepository.flush();

        //when
        activityService.setSwipingStatus(testActivity.getId(),"testToken1", SwipeStatus.TRUE);

        //then
        assertEquals(SwipeStatus.TRUE,activityRepository.findById(testActivity.getId()).get().getUserSwipeStatusList().get(0).getSwipeStatus());
    }

    @Test
    public void setSwipingStatus_WrongActivityID_throwsException(){
        //given
        User testUser = new User();
        testUser.setEmail("test.usertest@uzh.ch");
        testUser.setToken("testToken1");
        testUser.setName("newTester");
        testUser.setPassword("testPassword");
        testUser.setLastSeen(LocalDateTime.now());
        userRepository.save(testUser);
        userRepository.flush();
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(28L);
        ActivityPreset activityPreset = new ActivityPreset();
        activityPreset.setId(21L);
        activityPreset.setActivityName("test");
        activityPreset.setActivityCategory(ActivityCategory.SHOPPING);
        activityPreset.setGooglePOICategory("test");
        activityPreset.setGooglePOIKeyword("test");
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity= activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () ->activityService.setSwipingStatus(5L,"testToken1", SwipeStatus.TRUE));
    }

    @Test
    public void setSwipingStatus_WrongUserID_throwsException(){
        //given
        User testUser = new User();
        testUser.setEmail("test.usertest@uzh.ch");
        testUser.setToken("testToken1");
        testUser.setName("newTester");
        testUser.setPassword("testPassword");
        testUser.setLastSeen(LocalDateTime.now());
        userRepository.save(testUser);
        userRepository.flush();
        User wrongUser = new User();
        wrongUser.setEmail("wrongtest.user@uzh.ch");
        wrongUser.setToken("wrongToken");
        wrongUser.setName("wrongTester");
        wrongUser.setPassword("testPassword");
        wrongUser.setLastSeen(LocalDateTime.now());
        userRepository.save(wrongUser);
        userRepository.flush();
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = new ActivityPreset();
        activityPreset.setActivityName("test");
        activityPreset.setActivityCategory(ActivityCategory.SIGHTSEEING);
        activityPreset.setGooglePOICategory("test");
        activityPreset.setGooglePOIKeyword("test");
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity= activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () ->activityService.setSwipingStatus(5L,"wrongToken", SwipeStatus.TRUE));
    }

    @Test
    public void setSwipingStatus_TokenNotExists_throwsException(){
        //given
        User testUser = new User();
        testUser.setEmail("test.usertest@uzh.ch");
        testUser.setToken("testToken1");
        testUser.setName("newTester");
        testUser.setPassword("testPassword");
        testUser.setLastSeen(LocalDateTime.now());
        userRepository.save(testUser);
        userRepository.flush();
        Activity testActivity = new Activity();
        testActivity.setCreationDate(new Date());
        ArrayList<UserSwipeStatus> userSwipeStatusList = new ArrayList<UserSwipeStatus>();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        testActivity.setId(5L);
        ActivityPreset activityPreset = new ActivityPreset();
        activityPreset.setActivityName("test");
        activityPreset.setActivityCategory(ActivityCategory.GAMES);
        activityPreset.setGooglePOICategory("test");
        activityPreset.setGooglePOIKeyword("test");
        testActivity.setActivityPreset(activityPreset);
        userSwipeStatus.setId(1L);
        userSwipeStatus.setUser(testUser);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusRepository.save(userSwipeStatus);
        userSwipeStatusRepository.flush();
        userSwipeStatusList.add(userSwipeStatus);
        testActivity.setUserSwipeStatusList(userSwipeStatusList);
        testActivity= activityRepository.save(testActivity);
        activityRepository.flush();

        //then
        assertThrows(ResponseStatusException.class, () ->activityService.setSwipingStatus(5L,"notExistingToken", SwipeStatus.TRUE));
    }
 */
}