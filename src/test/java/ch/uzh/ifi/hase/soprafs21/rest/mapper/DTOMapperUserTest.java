package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.AgeRange;
import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOPublic;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperUserTest {
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
        LocalDate dateOfBirth = LocalDate.of(2000,2,3);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setToken("1");
        user.setPhone("079 122 34 54");
        user.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORTS);
        activityInterests.add(ActivityCategory.COOKING);
        userInterests.setActivityInterests(activityInterests);
        user.setUserInterests(userInterests);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getEmail(), userGetDTO.getEmail());
        assertEquals(user.getLastSeen(), userGetDTO.getLastSeen());
        assertEquals(user.getName(),userGetDTO.getName());
        assertEquals(user.getDateOfBirth(),userGetDTO.getDateOfBirth());
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
        LocalDate dateOfBirth = LocalDate.of(2000,2,3);
        userPutDTO.setDateOfBirth(dateOfBirth);
        userPutDTO.setGender(Gender.MALE);
        userPutDTO.setBio("asdf");
        userPutDTO.setPhone("079 122 34 54");
        userPutDTO.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORTS);
        activityInterests.add(ActivityCategory.COOKING);
        userInterests.setActivityInterests(activityInterests);
        userPutDTO.setUserInterests(userInterests);

        // MAP -> Create User
        User user = DTOMapperUser.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getEmail(), user.getEmail());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
        assertEquals(userPutDTO.getName(),user.getName());
        assertEquals(userPutDTO.getDateOfBirth(), user.getDateOfBirth());
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
        LocalDate dateOfBirth = LocalDate.of(2000,2,3);
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setPhone("079 122 34 54");
        user.setProfilePicture("String");

        UserInterests userInterests = new UserInterests();

        userInterests.setAgeRange(new AgeRange(18,23));
        userInterests.setGenderPreference(GenderPreference.EVERYONE);
        Set<ActivityCategory> activityInterests =  new HashSet<>();
        activityInterests.add(ActivityCategory.SPORTS);
        activityInterests.add(ActivityCategory.COOKING);
        userInterests.setActivityInterests(activityInterests);
        user.setUserInterests(userInterests);

        // MAP -> Create UserGetDTOProfile
        UserGetDTOPublic userGetDTOPublic = DTOMapperUser.INSTANCE.convertEntityToUserGetDTOProfile(user);

        // check content
        assertEquals(user.getLastSeen(), userGetDTOPublic.getLastSeen());
        assertEquals(user.getName(), userGetDTOPublic.getName());
        assertEquals(user.getDateOfBirth(), userGetDTOPublic.getDateOfBirth());
        assertEquals(user.getGender(), userGetDTOPublic.getGender());
        assertEquals(user.getBio(), userGetDTOPublic.getBio());
        assertEquals(user.getPhone(), userGetDTOPublic.getPhone());
        assertEquals(user.getProfilePicture(), userGetDTOPublic.getProfilePicture());
        assertEquals(user.getUserInterests(), userGetDTOPublic.getUserInterests());

        // check user interest content
        assertEquals(user.getUserInterests().getAgeRange().max, userGetDTOPublic.getUserInterests().getAgeRange().max);
        assertEquals(user.getUserInterests().getAgeRange().min, userGetDTOPublic.getUserInterests().getAgeRange().min);
        assertEquals(user.getUserInterests().getGenderPreference(), userGetDTOPublic.getUserInterests().getGenderPreference());
        assertEquals(user.getUserInterests().getActivityInterests(), userGetDTOPublic.getUserInterests().getActivityInterests());
    }
}
