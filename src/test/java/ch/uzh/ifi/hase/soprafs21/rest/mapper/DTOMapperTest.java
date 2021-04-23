package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ActivityPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("name@uzh.ch");
        userPostDTO.setPassword("blabla");
        userPostDTO.setName("Tester");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getEmail(), user.getEmail());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getName(), user.getName());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setEmail("firstname@lastname");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setName("test");
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getEmail(), userGetDTO.getEmail());
        assertEquals(user.getLastSeen(), userGetDTO.getLastSeen());
        assertEquals(user.getName(),userGetDTO.getName());
        assertEquals(user.getGender(),userGetDTO.getGender());
        assertEquals(user.getBio(),userGetDTO.getBio());
        assertEquals(user.getToken(),userGetDTO.getToken());
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
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user);
        List<UserSwipeStatus> userSwipeStatusList = Collections.singletonList(userSwipeStatus);

        // set activity preset
        ActivityPreset activityPreset = new ActivityPreset("Test Activity", ActivityCategory.MUSIC);

        // combine user swipe status and activity preset to activity
        Activity activity = new Activity(activityPreset, userSwipeStatusList);
        List<Activity> activityList = Collections.singletonList(activity);

        // map
        List <ActivityGetDTO> activityGetDTOList = DTOMapper.INSTANCE.convertEntityListToActivityGetDTOList(activityList);

        // check content
        assertEquals(activityList.get(0).getActivityPreset(), activityGetDTOList.get(0).getActivityPreset());
        assertEquals(activityList.get(0).getUserSwipeStatusList(), activityGetDTOList.get(0).getUserSwipeStatusList());
    }

    @Test
    public void test_ActivityPutDTO_to_Activity() {
        // set user swipe status
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(null);
        List<UserSwipeStatus> userSwipeStatusList = Collections.singletonList(userSwipeStatus);
        ActivityPutDTO activityPutDTO = new ActivityPutDTO();
        activityPutDTO.setUserSwipeStatusList(userSwipeStatusList);

        // map
        Activity activity = DTOMapper.INSTANCE.convertActivityPutDTOtoEntity(activityPutDTO);

        // check content
        assertEquals(activityPutDTO.getUserSwipeStatusList(), activity.getUserSwipeStatusList());
    }

}
