package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DTOMapperActivityTest {
    @Test
    public void testGetActivity_fromActivity_toActivityGetDTO_success() {
        // create activity
        Activity activity = new Activity();
        activity.setId(1L);

        // do the setting twice to test constructor and setters
        ActivityPreset activityPreset = new ActivityPreset("play football", ActivityCategory.SPORTS,"sport","football", "TestImageURL");
        activityPreset.setActivityName("play football");
        activityPreset.setActivityCategory(ActivityCategory.SPORTS);
        activityPreset.setGooglePOICategory("sport");
        activityPreset.setGooglePOIKeyword("football");
        activity.setActivityPreset(activityPreset);

        // do the setting twice to test constructor and setters
        List<UserSwipeStatus> userSwipeStatusList= new ArrayList<>();
        User user = new User();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, SwipeStatus.INITIAL);
        userSwipeStatus.setUser(user);
        userSwipeStatus.setSwipeStatus(SwipeStatus.INITIAL);
        userSwipeStatusList.add(userSwipeStatus);
        activity.setUserSwipeStatusList(userSwipeStatusList);

        // MAP -> Create ActivityGetDTO
        ActivityGetDTO activityGetDTO = DTOMapperActivity.INSTANCE.convertEntityToActivityGetDTO(activity);

        // check content
        assertEquals(activity.getId(), activityGetDTO.getId());
        assertEquals(activity.getActivityPreset(),activityGetDTO.getActivityPreset());
        assertEquals(activity.getUserSwipeStatusList(),activity.getUserSwipeStatusList());

        // check activity preset content
        assertEquals(activity.getActivityPreset().getActivityName(),activityGetDTO.getActivityPreset().getActivityName());
        assertEquals(activity.getActivityPreset().getActivityCategory(),activityGetDTO.getActivityPreset().getActivityCategory());
        assertEquals(activity.getActivityPreset().getGooglePOICategory(),activityGetDTO.getActivityPreset().getGooglePOICategory());
        assertEquals(activity.getActivityPreset().getGooglePOIKeyword(),activityGetDTO.getActivityPreset().getGooglePOIKeyword());

        // check swipeStatusList content
        assertEquals(activity.getUserSwipeStatusList().get(0).getUser(),activityGetDTO.getUserSwipeStatusList().get(0).getUser());
        assertEquals(activity.getUserSwipeStatusList().get(0).getSwipeStatus(),activityGetDTO.getUserSwipeStatusList().get(0).getSwipeStatus());
    }


    @Test
    public void test_Activity_to_ActivityGetDTO() {
        // create user
        User user = new User();
        user.setId(1L);
        user.setEmail("firstname@lastname");
        user.setPassword("verySafePassword");
        user.setToken("token123");

        // set user swipe status
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, SwipeStatus.INITIAL);
        List<UserSwipeStatus> userSwipeStatusList = Collections.singletonList(userSwipeStatus);

        // set activity preset
        ActivityPreset activityPreset = new ActivityPreset("Test Activity", ActivityCategory.MUSIC);

        // combine user swipe status and activity preset to activity
        Activity activity = new Activity(activityPreset, userSwipeStatusList);
        List<Activity> activityList = Collections.singletonList(activity);

        // map
        List <ActivityGetDTO> activityGetDTOList = DTOMapperActivity.INSTANCE.convertEntityListToActivityGetDTOList(activityList);

        // check content
        assertEquals(activityList.get(0).getActivityPreset(), activityGetDTOList.get(0).getActivityPreset());
        assertEquals(activityList.get(0).getUserSwipeStatusList(), activityGetDTOList.get(0).getUserSwipeStatusList());
    }

}