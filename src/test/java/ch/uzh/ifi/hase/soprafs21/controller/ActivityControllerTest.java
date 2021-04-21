package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

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
        mockMvc.perform(putRequest);

        Mockito.verify(activityService, Mockito.times(1)).setSwipingStatus(1L,"123",SwipeStatus.TRUE);

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