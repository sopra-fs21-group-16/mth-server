package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.*;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTOProfile;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

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
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

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
        UserGetDTOProfile userGetDTOProfile = DTOMapper.INSTANCE.convertEntityToUserGetDTOProfile(user);

        // check content
        assertEquals(user.getLastSeen(), userGetDTOProfile.getLastSeen());
        assertEquals(user.getName(),userGetDTOProfile.getName());
        assertEquals(user.getGender(),userGetDTOProfile.getGender());
        assertEquals(user.getBio(),userGetDTOProfile.getBio());
        assertEquals(user.getPhone(),userGetDTOProfile.getPhone());
        assertEquals(user.getProfilePicture(),userGetDTOProfile.getProfilePicture());
        assertEquals(user.getUserInterests(),userGetDTOProfile.getUserInterests());
    }
}
