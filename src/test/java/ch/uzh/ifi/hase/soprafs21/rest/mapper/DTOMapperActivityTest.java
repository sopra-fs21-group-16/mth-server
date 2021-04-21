package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperActivityTest {
    @Test
    public void testGetActivity_fromActivity_toActivityGetDTO_success() {
        // create activity
        Activity activity = new Activity();
        activity.setId(1L);

        // do the setting twice to test constructor and setters
        ActivityPreset activityPreset = new ActivityPreset("play football", ActivityCategory.SPORT,"sport","football");
        activityPreset.setActivityName("play football");
        activityPreset.setActivityCategory(ActivityCategory.SPORT);
        activityPreset.setGooglePOICategory("sport");
        activityPreset.setGooglePOIKeyword("football");
        activity.setActivityPreset(activityPreset);

        // do the setting twice to test constructor and setters
        List<UserSwipeStatus> userSwipeStatusList= new ArrayList<>();
        User user = new User();
        /** TODO: Adapt Test to insert UserSwipeStatus.INITIAL instead of null */
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, null);
        userSwipeStatus.setUser(user);
        userSwipeStatus.setSwipeStatus(null);
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
}