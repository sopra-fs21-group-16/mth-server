package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.service.SchedulingService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SchedulesController.class)
class SchedulesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchedulingService schedulingService;

    @MockBean
    private UserService userService;

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
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"Scheduling session with session id " + givenSessionIdFromHeader + " was not found")).when(schedulingService).deleteScheduledSession(givenSessionIdFromHeader);

        MockHttpServletRequestBuilder deleteRequest = delete("/schedules/" + givenSessionIdFromHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", "Token");

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());
    }
}