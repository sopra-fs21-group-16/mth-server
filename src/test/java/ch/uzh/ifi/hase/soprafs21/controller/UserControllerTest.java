package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@uzh.ch");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test@uzh.ch");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    public void loginUser_validInput_TokenReturned() throws Exception {
        // given
        User user = new User();
        user.setToken("1");
        user.setPassword("testPassword");
        user.setEmail("test@uzh.ch");


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test@uzh.ch");
        userPostDTO.setPassword("testPassword");


        given(userService.loginUser(Mockito.any())).willReturn(user.getToken());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result=mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(content, user.getToken());
    }

    @Test
    void logoutUser() throws Exception{
        // given
        User user = new User();
        user.setId(1L);
        user.setToken("123");
        user.setEmail("test@uzh.ch");

        // when
        MockHttpServletRequestBuilder postRequest = post("/users/1/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", user.getToken());

        given(userService.isUserAuthenticated(user.getId(),user.getToken())).willReturn(Boolean.TRUE);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }

    @Test
    void getUserOverview() {
    }

    @Test
    void userPing() {
    }

    @Test
    void createUserProfile() {
    }

    @Test
    void updateUserProfile() {
    }

    @Test
    void getUserProfile() {
    }

    @Test
    void verifyUserProfile() {
    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
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