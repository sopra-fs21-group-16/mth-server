package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperUser;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void createUser_invalidInput_userNotCreated() throws Exception {
        // given a user that already has the same email as the new one wants to have
        User user = new User();
        user.setId(1L);
        user.setEmail("test@uzh.ch");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test@uzh.ch");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"The email provided is not unique and already used. Please use another email!")).when(userService).createUser(Mockito.any());

        // when
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
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
    public void updateUserProfile_Success() throws Exception{
        // user in repo
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setGender(null);
        userFromRepo.setToken("ssfs");

        // changed user
        User changedUser = new User();
        changedUser.setGender(Gender.MALE);

        // user object containing new changes for user in repo
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setGender(Gender.MALE);
        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        User userInput = DTOMapperUser.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        given(userService.isUserAuthenticated(idFromURI,tokenFromHeader)).willReturn(true);

        given(userService.getUserByID(idFromURI)).willReturn(userFromRepo);

        given(userService.isUserAuthorized(idFromURI,userFromRepo.getId(),tokenFromHeader)).willReturn(true);

        doNothing().when(userService).applyUserProfileChange(userInput,userFromRepo);

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", tokenFromHeader);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateUserProfile_With_Invalid_Profile() throws Exception{
        // user in repo
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setGender(null);
        userFromRepo.setToken("ssfs");

        // user object containing new changes for user in repo --> containing no change data
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setGender(null);
        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        given(userService.isUserAuthenticated(idFromURI,tokenFromHeader)).willReturn(true);

        given(userService.getUserByID(idFromURI)).willReturn(userFromRepo);

        given(userService.isUserAuthorized(idFromURI,userFromRepo.getId(),tokenFromHeader)).willReturn(true);

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"No change data was provided")).when(userService).applyUserProfileChange(Mockito.any(),Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", tokenFromHeader);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserProfile_For_Not_Existing_User() throws Exception{
        // no user in repo

        // user object containing new changes for user in repo --> containing no change data
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setGender(Gender.MALE);
        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"Provided user could not be found.")).when(userService).isUserAuthenticated(idFromURI,tokenFromHeader);

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", tokenFromHeader);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserProfile_With_Invalid_Token() throws Exception{
        // user in repo
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setGender(null);
        userFromRepo.setToken("ssfs");

        // user object containing new changes for user in repo --> containing no change data
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setGender(Gender.MALE);
        String tokenFromHeader = "notTheSameTokenAsInRepo";
        Long idFromURI = 1L;

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provided user does not match Auth-Token.")).when(userService).isUserAuthenticated(idFromURI,tokenFromHeader);

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", tokenFromHeader);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserProfile_Success() throws Exception{
        // user in repo that should be returned
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setEmail("test@uzh.ch");
        userFromRepo.setName("testname");
        userFromRepo.setBio("bio");

        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        doNothing().when(userService).checkIfUserExistsWithGivenId(idFromURI);

        given(userService.checkIfValidToken(tokenFromHeader)).willReturn(true);

        given(userService.getUserByID(idFromURI)).willReturn(userFromRepo);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", tokenFromHeader);

        mockMvc.perform(getRequest)  // mockMbc simulates HTTP request on given URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userFromRepo.getName())))
                .andExpect(jsonPath("$.bio", is(userFromRepo.getBio())));
    }

    @Test
    void getUserProfile_Invalid_Profile() throws Exception{
        // user in repo that should be returned --> has not the matching id
        User userFromRepo = new User();
        userFromRepo.setId(2L);
        userFromRepo.setEmail("test@uzh.ch");

        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"Provided user could not be found.")).when(userService).checkIfUserExistsWithGivenId(idFromURI);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", tokenFromHeader);

        mockMvc.perform(getRequest)  // mockMbc simulates HTTP request on given URL
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserProfile_Invalid_Token() throws Exception{
        // user in repo that should be returned, but it is not since invalid token
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setEmail("test@uzh.ch");

        // invalid token
        String tokenFromHeader = "invalid";
        Long idFromURI = 1L;

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid")).when(userService).checkIfValidToken(tokenFromHeader);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/" + idFromURI + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", tokenFromHeader);

        mockMvc.perform(getRequest)  // mockMbc simulates HTTP request on given URL
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserProfiles_Success() throws Exception{
        // user in repo that should be returned
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setEmail("test@uzh.ch");
        userFromRepo.setName("testname");
        userFromRepo.setBio("bio");

        String tokenFromHeader = "ssfs";
        Long idFromURI = 1L;

        // saves a list that contains only one object
        List<User> allUsers = Collections.singletonList(userFromRepo);

        given(userService.checkIfValidToken(tokenFromHeader)).willReturn(true);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", tokenFromHeader);

        mockMvc.perform(getRequest)  // mockMbc simulates HTTP request on given URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(userFromRepo.getName())))
                .andExpect(jsonPath("$[0].bio", is(userFromRepo.getBio())));
    }

    @Test
    void getUserProfiles_Invalid_Token() throws Exception{
        // user in repo that should be returned
        User userFromRepo = new User();
        userFromRepo.setId(1L);
        userFromRepo.setEmail("test@uzh.ch");
        userFromRepo.setName("testname");
        userFromRepo.setBio("bio");

        // invalid token
        String tokenFromHeader = "invalid";
        Long idFromURI = 1L;

        // saves a list that contains only one object
        List<User> allUsers = Collections.singletonList(userFromRepo);

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid")).when(userService).checkIfValidToken(tokenFromHeader);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", tokenFromHeader);

        mockMvc.perform(getRequest)  // mockMbc simulates HTTP request on given URL
                .andExpect(status().isUnauthorized());
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