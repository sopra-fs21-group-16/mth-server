package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void test_getActivities() throws Exception {
        // given
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

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(activityService.getActivities(user.getId(), user.getToken())).willReturn(activityList);
        given(activityService.getActivities(user.getId(), user.getToken())).willReturn(activityList);

        // when
        MockHttpServletRequestBuilder getRequest = get("/activities/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON).header("Auth-Token", user.getToken());

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
                // ToDo: test more? e.g. from User: .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }
}
