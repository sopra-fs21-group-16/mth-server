package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.*;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userSwipeStatusDTO.UserSwipeStatusGetDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DTOMapperUserSwipeStatusTest {
    @Test
    public void testGetUserSwipeStatus_fromUserSwipeStatus_toUserSwipeStatusGetDTO_success(){
        // create user
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

        // create userSwipeStatus
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, SwipeStatus.TRUE);

        UserSwipeStatusGetDTO userSwipeStatusGetDTO = DTOMapperUserSwipeStatus.INSTANCE.convertEntityToUserSwipeStatusGetDTO(userSwipeStatus);

        // check content
        assertEquals(userSwipeStatus.getUser().getId(), userSwipeStatusGetDTO.getUser().getId());
        assertEquals(userSwipeStatus.getUser().getLastSeen(), userSwipeStatusGetDTO.getUser().getLastSeen());
        assertEquals(userSwipeStatus.getUser().getName(),userSwipeStatusGetDTO.getUser().getName());
        assertEquals(userSwipeStatus.getUser().getGender(),userSwipeStatusGetDTO.getUser().getGender());
        assertEquals(userSwipeStatus.getUser().getBio(),userSwipeStatusGetDTO.getUser().getBio());
        assertEquals(userSwipeStatus.getUser().getPhone(),userSwipeStatusGetDTO.getUser().getPhone());
        assertEquals(userSwipeStatus.getUser().getProfilePicture(),userSwipeStatusGetDTO.getUser().getProfilePicture());
        assertEquals(userSwipeStatus.getUser().getUserInterests(),userSwipeStatusGetDTO.getUser().getUserInterests());
        assertEquals(userSwipeStatus.getSwipeStatus(),userSwipeStatusGetDTO.getSwipeStatus());
    }

    @Test
    public void testGetUserSwipeStatusList_fromUserSwipeStatusList_toUserSwipeStatusListGetDTO(){
        // create user
        User user = new User();
        user.setId(1L);
        user.setEmail("firstname@lastname");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setName("test");

        // create userSwipeStatus
        UserSwipeStatus userSwipeStatus = new UserSwipeStatus(user, SwipeStatus.TRUE);
        List<UserSwipeStatus> userSwipeStatusList = Collections.singletonList(userSwipeStatus);

        List<UserSwipeStatusGetDTO> userSwipeStatusGetDTOList = DTOMapperUserSwipeStatus.INSTANCE.convertEntityListToUserSwipeStatusGetDTOList(userSwipeStatusList);

        // check content
        assertEquals(userSwipeStatusList.get(0).getUser().getId(),userSwipeStatusGetDTOList.get(0).getUser().getId());
        assertEquals(userSwipeStatusList.get(0).getUser().getName(),userSwipeStatusGetDTOList.get(0).getUser().getName());
        assertEquals(userSwipeStatusList.get(0).getSwipeStatus(),userSwipeStatusGetDTOList.get(0).getSwipeStatus());
    }
}