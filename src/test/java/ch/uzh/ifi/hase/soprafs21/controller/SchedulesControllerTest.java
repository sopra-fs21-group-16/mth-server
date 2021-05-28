package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.UserIdsSchedulingPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.SchedulingService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SchedulesControllerTest
 * This is a WebMvcTest which allows to test the SchedulesController i.e. GET/POST request without actually sending them over the network.
 * This tests if the SchedulesController works.
 */
@WebMvcTest(SchedulesController.class)
class SchedulesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchedulingService schedulingService;

    @MockBean
    private UserService userService;

    @Test
    void createSchedulingSession_validInputs_schedulingSessionCreated() throws Exception {
        UserIdsSchedulingPostDTO userIdsSchedulingPostDTO = new UserIdsSchedulingPostDTO();
        userIdsSchedulingPostDTO.setUserId1(1L);
        userIdsSchedulingPostDTO.setUserId2(2L);

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        SchedulingSession schedulingSession = new SchedulingSession();
        given(schedulingService.createSchedulingSession(1L, 2L, "offer","Token")).willReturn(schedulingSession);

        MockHttpServletRequestBuilder postRequest = post("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userIdsSchedulingPostDTO))
                .header("Auth-Token", "Token");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }


    @Test
    void getSchedulingSessionOfUser_validInputs() throws Exception {
        List<Long> IDs = new ArrayList<Long>();

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        given(schedulingService.getSchedulingSessionsOfUser("TOken")).willReturn(IDs);

        MockHttpServletRequestBuilder getRequest = get("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", "Token");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());


    }


    @Test
    void getScheduledActivity() throws Exception {
        ScheduledActivity scheduledActivity = new ScheduledActivity();
        scheduledActivity.setLocation("TestLocation");
        ScheduledActivityPostDTO scheduledActivityPostDTO = new ScheduledActivityPostDTO();
        scheduledActivityPostDTO.setLocation("TestLocation");

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        given(schedulingService.saveScheduledActivity(Mockito.anyLong(), Mockito.any())).willReturn(scheduledActivity);

        MockHttpServletRequestBuilder postRequest = post("/schedules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(scheduledActivityPostDTO))
                .header("Auth-Token", "Token");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    void getProposedInformation() throws Exception {
        SchedulingSession schedulingSession = new SchedulingSession();

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        given(schedulingService.getSchedulingSession(Mockito.anyLong(), Mockito.any())).willReturn(schedulingSession);

        MockHttpServletRequestBuilder getRequest = get("/schedules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", "Token");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    void setProposedInformation() throws Exception {
        SchedulingSessionPutDTO schedulingSessionPutDTO = new SchedulingSessionPutDTO();
        SchedulingSession schedulingSession = new SchedulingSession();

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        Mockito.doNothing().when(schedulingService).updateSchedulingSession(Mockito.anyLong(), Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/schedules/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(schedulingSessionPutDTO))
            .header("Auth-Token", "Token");

        
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
   }
          
   @Test
    void deleteScheduledSession_success() throws Exception{
       SchedulingSession schedulingSession = new SchedulingSession();

       long givenSessionIdFromHeader = 1L;

       // when
       given(userService.checkIfValidToken("Token")).willReturn(true);
       doNothing().when(schedulingService).checkIfScheduledSessionExistsWithGivenId(givenSessionIdFromHeader);

       MockHttpServletRequestBuilder deleteRequest = delete("/schedules/" + givenSessionIdFromHeader)
               .contentType(MediaType.APPLICATION_JSON)
               .header("Auth-Token", "Token");

       // then
       mockMvc.perform(deleteRequest)
               .andExpect(status().isOk());
   }

    @Test
    void deleteScheduledSession_throwsException_NotFound() throws Exception{
        SchedulingSession schedulingSession = new SchedulingSession();

        long givenSessionIdFromHeader = 1L;

        // when
        given(userService.checkIfValidToken("Token")).willReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Scheduling session with session id " + givenSessionIdFromHeader + " was not found")).when(schedulingService).deleteScheduledSession(givenSessionIdFromHeader);

        MockHttpServletRequestBuilder deleteRequest = delete("/schedules/" + givenSessionIdFromHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", "Token");
      
        mockMvc.perform(deleteRequest)
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteScheduledActivity() throws Exception{
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
       
    }
}
