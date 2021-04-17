package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.*;
import ch.uzh.ifi.hase.soprafs21.entities.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOProfile;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        User user = DTOMapperUser.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getEmail(), user.getEmail());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getName(), user.getName());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setId(1L);
        user.setEmail("firstname@lastname");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setName("test");
        user.setAge(20);
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setToken("1");
        user.setPhone("079 122 34 54");
        user.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORT);
        activityInterests.add(ActivityCategory.FOOD);
        userInterests.setActivityInterests(activityInterests);
        user.setUserInterests(userInterests);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getEmail(), userGetDTO.getEmail());
        assertEquals(user.getLastSeen(), userGetDTO.getLastSeen());
        assertEquals(user.getName(),userGetDTO.getName());
        assertEquals(user.getGender(),userGetDTO.getGender());
        assertEquals(user.getBio(),userGetDTO.getBio());
        assertEquals(user.getToken(),userGetDTO.getToken());
        assertEquals(user.getPhone(),userGetDTO.getPhone());
        assertEquals(user.getProfilePicture(),userGetDTO.getProfilePicture());
        assertEquals(user.getUserInterests(),userGetDTO.getUserInterests());

        // check user interest content
        assertEquals(user.getUserInterests().getAgeRange().max,userGetDTO.getUserInterests().getAgeRange().max);
        assertEquals(user.getUserInterests().getAgeRange().min,userGetDTO.getUserInterests().getAgeRange().min);
        assertEquals(user.getUserInterests().getGenderPreference(),userGetDTO.getUserInterests().getGenderPreference());
        assertEquals(user.getUserInterests().getActivityInterests(),userGetDTO.getUserInterests().getActivityInterests());
    }

    @Test
    public void testPutUser_fromUserPutDTO_toUser_success() {
        // create User
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setEmail("firstname@lastname");
        userPutDTO.setPassword("password");
        userPutDTO.setName("test");
        userPutDTO.setAge(20);
        userPutDTO.setGender(Gender.MALE);
        userPutDTO.setBio("asdf");
        userPutDTO.setPhone("079 122 34 54");
        userPutDTO.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORT);
        activityInterests.add(ActivityCategory.FOOD);
        userInterests.setActivityInterests(activityInterests);
        userPutDTO.setUserInterests(userInterests);

        // MAP -> Create User
        User user = DTOMapperUser.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getEmail(), user.getEmail());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
        assertEquals(userPutDTO.getName(),user.getName());
        assertEquals(userPutDTO.getAge(), user.getAge());
        assertEquals(userPutDTO.getGender(),user.getGender());
        assertEquals(userPutDTO.getBio(),user.getBio());
        assertEquals(userPutDTO.getPhone(),user.getPhone());
        assertEquals(userPutDTO.getProfilePicture(),user.getProfilePicture());
        assertEquals(userPutDTO.getUserInterests(),user.getUserInterests());

        // check user interest content
        assertEquals(userPutDTO.getUserInterests().getAgeRange().max,user.getUserInterests().getAgeRange().max);
        assertEquals(userPutDTO.getUserInterests().getAgeRange().min,user.getUserInterests().getAgeRange().min);
        assertEquals(userPutDTO.getUserInterests().getGenderPreference(),user.getUserInterests().getGenderPreference());
        assertEquals(userPutDTO.getUserInterests().getActivityInterests(),user.getUserInterests().getActivityInterests());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTOProfile_success() {
        // create User
        User user = new User();
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setName("test");
        user.setAge(20);
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setPhone("079 122 34 54");
        user.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORT);
        activityInterests.add(ActivityCategory.FOOD);
        userInterests.setActivityInterests(activityInterests);
        user.setUserInterests(userInterests);

        // MAP -> Create UserGetDTOProfile
        UserGetDTOProfile userGetDTOProfile = DTOMapperUser.INSTANCE.convertEntityToUserGetDTOProfile(user);

        // check content
        assertEquals(user.getLastSeen(), userGetDTOProfile.getLastSeen());
        assertEquals(user.getName(),userGetDTOProfile.getName());
        assertEquals(user.getGender(),userGetDTOProfile.getGender());
        assertEquals(user.getBio(),userGetDTOProfile.getBio());
        assertEquals(user.getPhone(),userGetDTOProfile.getPhone());
        assertEquals(user.getProfilePicture(),userGetDTOProfile.getProfilePicture());
        assertEquals(user.getUserInterests(),userGetDTOProfile.getUserInterests());

        // check user interest content
        assertEquals(user.getUserInterests().getAgeRange().max,userGetDTOProfile.getUserInterests().getAgeRange().max);
        assertEquals(user.getUserInterests().getAgeRange().min,userGetDTOProfile.getUserInterests().getAgeRange().min);
        assertEquals(user.getUserInterests().getGenderPreference(),userGetDTOProfile.getUserInterests().getGenderPreference());
        assertEquals(user.getUserInterests().getActivityInterests(),userGetDTOProfile.getUserInterests().getActivityInterests());
    }

    @Test
    public void testPutActivity_fromActivityPutDTO_toActivity_success() {
        // create activityPutDTO
        ActivityPutDTO activityPutDTO = new ActivityPutDTO();
        activityPutDTO.setId(1L);

        // do the setting twice to test constructor and setters
        List<UserSwipeStatus> userSwipeStatusList= new ArrayList<>();
        User user = new User();
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, SwipeStatus.FALSE);
        userSwipeStatus.setUser(user);
        userSwipeStatus.setSwipeStatus(SwipeStatus.FALSE);
        userSwipeStatusList.add(userSwipeStatus);
        activityPutDTO.setUserSwipeStatusList(userSwipeStatusList);

        // MAP -> Create Activity
        Activity activity = DTOMapperActivity.INSTANCE.convertActivityPutDTOtoEntity(activityPutDTO);

        // check content
        assertEquals(activityPutDTO.getId(),activity.getId());
        assertEquals(activityPutDTO.getUserSwipeStatusList(),activity.getUserSwipeStatusList());

        // check swipeStatusList content
        assertEquals(activityPutDTO.getUserSwipeStatusList().get(0).getUser(),activity.getUserSwipeStatusList().get(0).getUser());
        assertEquals(activityPutDTO.getUserSwipeStatusList().get(0).getSwipeStatus(),activity.getUserSwipeStatusList().get(0).getSwipeStatus());
    }

    @Test
    public void testGetActivity_fromActivity_toActivityGetDTO_success() {
        // create activity
        Activity activity = new Activity();
        activity.setId(1L);

        // do the setting twice to test constructor and setters
        ActivityPreset activityPreset = new ActivityPreset("play football",ActivityCategory.SPORT,"sport","football");
        activityPreset.setActivityName("play football");
        activityPreset.setActivityCategory(ActivityCategory.SPORT);
        activityPreset.setGooglePOICategory("sport");
        activityPreset.setGooglePOIKeyword("football");
        activity.setActivityPreset(activityPreset);

        // MAP -> Create ActivityGetDTO
        ActivityGetDTO activityGetDTO = DTOMapperActivity.INSTANCE.convertEntityToActivityGetDTO(activity);

        // check content
        assertEquals(activity.getId(), activityGetDTO.getId());
        assertEquals(activity.getActivityPreset(),activityGetDTO.getActivityPreset());

        // check activity preset content
        assertEquals(activity.getActivityPreset().getActivityName(),activityGetDTO.getActivityPreset().getActivityName());
        assertEquals(activity.getActivityPreset().getActivityCategory(),activityGetDTO.getActivityPreset().getActivityCategory());
        assertEquals(activity.getActivityPreset().getGooglePOICategory(),activityGetDTO.getActivityPreset().getGooglePOICategory());
        assertEquals(activity.getActivityPreset().getGooglePOIKeyword(),activityGetDTO.getActivityPreset().getGooglePOIKeyword());
    }
}
