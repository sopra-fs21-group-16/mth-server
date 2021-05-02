package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionPutDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperSchedulingTest {
    @Test
    public void testGetScheduledActivity_fromScheduledActivity_toScheduledActivityGetDTO_success() {
        // create activity
        Activity activity = new Activity();
        activity.setId(1L);

        // do the setting twice to test constructor and setters
        ActivityPreset activityPreset = new ActivityPreset("play football", ActivityCategory.SPORTS, "sport", "football");
        activityPreset.setActivityName("play football");
        activityPreset.setActivityCategory(ActivityCategory.SPORTS);
        activityPreset.setGooglePOICategory("sport");
        activityPreset.setGooglePOIKeyword("football");
        activity.setActivityPreset(activityPreset);

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

    @Test
    public void testPostScheduledActivity_fromScheduledActivityPostDTO_toScheduledActivityEntity_success() {
        // create ScheduledActivityPostDTO
        ScheduledActivityPostDTO scheduledActivityPostDTO = new ScheduledActivityPostDTO();

        // create activity
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setUserSwipeStatusList(new ArrayList<UserSwipeStatus>());
        scheduledActivityPostDTO.setActivity(activity);
        scheduledActivityPostDTO.setDate(LocalDateTime.now());
        scheduledActivityPostDTO.setLocation("TestLocation");



        // MAP -> Create ScheduledActivityGetDTO
        ScheduledActivity scheduledActivity = DTOMapperScheduling.INSTANCE.convertScheduledActivityPostDTOToEntity(scheduledActivityPostDTO);

        // check content
        assertEquals(scheduledActivityPostDTO.getActivity(), scheduledActivity.getActivity());
        assertEquals(scheduledActivityPostDTO.getDate(), scheduledActivity.getDate());
        assertEquals(scheduledActivityPostDTO.getLocation(), scheduledActivity.getLocation());
    }

    @Test
    public void testPutSchedulingSession_fromSchedulingSessionPutDTO_toSchedulingSessionEntity_success() {
        // create SchedulingSessionPutDTO
        SchedulingSessionPutDTO schedulingSessionPutDTO = new SchedulingSessionPutDTO();

        // create activity
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setUserSwipeStatusList(new ArrayList<UserSwipeStatus>());
        Activity activity2 = new Activity();
        activity2.setId(2L);
        activity2.setUserSwipeStatusList(new ArrayList<UserSwipeStatus>());

        schedulingSessionPutDTO.setChosenActivity(activity);
        schedulingSessionPutDTO.setChosenDate(LocalDateTime.now());
        schedulingSessionPutDTO.setChosenLocation("TestLocation");
        List<Activity> activityList = new ArrayList<>();
        activityList.add(activity);
        activityList.add(activity2);
        schedulingSessionPutDTO.setActivityList(activityList);
        List<LocalDateTime> dateList = new ArrayList<>();
        dateList.add(LocalDateTime.now());
        schedulingSessionPutDTO.setDateList(dateList);
        List<String> locationList = new ArrayList<>();
        locationList.add("TestLocation");
        schedulingSessionPutDTO.setLocationList(locationList);

        // MAP -> Create ScheduledActivityGetDTO
        SchedulingSession schedulingSession = DTOMapperScheduling.INSTANCE.convertSchedulingSessionPutDTOToEntity(schedulingSessionPutDTO);

        // check content
        assertEquals(schedulingSession.getChosenActivity(), schedulingSessionPutDTO.getChosenActivity());
        assertEquals(schedulingSession.getChosenDate(), schedulingSessionPutDTO.getChosenDate());
        assertEquals(schedulingSession.getChosenLocation(), schedulingSessionPutDTO.getChosenLocation());
        assertEquals(schedulingSession.getActivityList(), schedulingSessionPutDTO.getActivityList());
        assertEquals(schedulingSession.getDateList(), schedulingSessionPutDTO.getDateList());
        assertEquals(schedulingSession.getLocationList(), schedulingSessionPutDTO.getLocationList());
    }
}
