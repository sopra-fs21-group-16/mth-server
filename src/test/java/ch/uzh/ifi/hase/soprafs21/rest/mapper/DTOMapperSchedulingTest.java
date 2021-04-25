package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityGetDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperSchedulingTest {
    @Test
    public void testGetScheduledActivity_fromScheduledActivity_toScheduledActivityGetDTO_success() {
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
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus();
        userSwipeStatus.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatus.setUser(user);
        userSwipeStatus.setSwipeStatus(SwipeStatus.TRUE);
        userSwipeStatusList.add(userSwipeStatus);
        activity.setUserSwipeStatusList(userSwipeStatusList);
        ScheduledActivity scheduledActivity = new ScheduledActivity();

        scheduledActivity.setId(1L);
        scheduledActivity.setActivity(activity);
        scheduledActivity.setDate(LocalDateTime.now());
        scheduledActivity.setLocation("TestLocation");

        // MAP -> Create ScheduledActivityGetDTO
        ScheduledActivityGetDTO scheduledActivityGetDTO = DTOMapperScheduling.INSTANCE.convertEntityToScheduledActivityGetDTO(scheduledActivity);

        // check content
        assertEquals(scheduledActivity.getId(), scheduledActivityGetDTO.getId());
        assertEquals(scheduledActivity.getDate(), scheduledActivityGetDTO.getDate());
        assertEquals(scheduledActivity.getLocation(), scheduledActivityGetDTO.getLocation());

        // check activity content
        assertEquals(scheduledActivity.getActivity(), scheduledActivityGetDTO.getActivity());
        assertEquals(scheduledActivity.getLocation(), scheduledActivityGetDTO.getLocation());
        assertEquals(scheduledActivity.getDate(), scheduledActivityGetDTO.getDate());
    }
}
