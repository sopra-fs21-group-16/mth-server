package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;
  
    @MockBean
    private UserService userService;

    @Test
    public void test_getActivityCategories() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/activitycategories/");
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(ActivityCategory.values().length))); // check that amount of json entries corresponds to enum entries
    }

    @Test
    public void test_getActivities() throws Exception {
        // given
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

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(activityService.getActivities(user.getToken())).willReturn(activityList);

        // when
        MockHttpServletRequestBuilder getRequest = get("/activities/")
                .contentType(MediaType.APPLICATION_JSON).header("Auth-Token", user.getToken());

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        // ToDo: test more? e.g. from User: .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    public void getActivities_validInput_returnsActivities() throws Exception { }

    @Test
    public void updateSwipeStatus_validInput_StatusUpdated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setToken("123");

        // when
        MockHttpServletRequestBuilder putRequest = put("/activities/swipe/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", user.getToken())
                .content(asJsonString(SwipeStatus.TRUE));

        //then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());

        Mockito.verify(activityService, Mockito.times(1)).setSwipingStatus(1L, "123", SwipeStatus.TRUE);

    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}