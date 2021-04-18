package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.repository.ActivityRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private UserRepository userRepository;

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
        testActivity.setUserSwipeStatusList(new ArrayList<UserSwipeStatus>());

        //when
        Mockito.when(userRepository.findByToken("testToken")).thenReturn(testUser);
        Mockito.when(activityRepository.findByID(5L)).thenReturn(testActivity);

        //then
        activityService.setSwipingStatus(5L,"testToken", SwipeStatus.TRUE);

        Mockito.verify(activityRepository, Mockito.times(1)).save(Mockito.any());
    }

}