package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOProfile;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperUser;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapperUser.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapperUser.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // login user and return token as String
        return DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(userService.loginUser(userInput));
    }

    @PostMapping("/users/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logout(@RequestHeader("Auth-Token")String token){

        // checks if token is valid
        userService.checkIfValidToken(token);

        // get id of user
        long userId = userService.getIdByToken(token);

        userService.logOutUser(userId);
    }

    @GetMapping("/users/ping")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userPing(@RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        // additional step necessary
    }

    @PutMapping("/users/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUserProfile(@RequestBody UserPutDTO userPutDTOProfile, @RequestHeader("Auth-Token")String token){
        // convert API user to internal representation
        User userInput = DTOMapperUser.INSTANCE.convertUserPutDTOtoEntity(userPutDTOProfile);

        // check if provided token is valid
        userService.checkIfValidToken(token);

        // get id of user
        long userId = userService.getIdByToken(token);

        // find the user in the repo that holds resource to be changed
        User userFromRepo = userService.getUserByID(userId);

        // Checks whether the user is authenticated and authorized to use a resource, given the resource owner userId
        userService.isUserAuthorized(userId, userFromRepo.getId(), token);

        // creates user profile
        userService.applyUserProfileChange(userInput,userFromRepo);
    }

    @GetMapping("/users/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getUserProfile(@RequestHeader("Auth-Token")String token){

        // checks if the visitor has a valid token
        userService.checkIfValidToken(token);

        // get user profile
        User userFromRepo = userService.getUserByToken(token);

        userService.isUserAuthenticated(userService.getIdByToken(token),token);

        return DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(userFromRepo);
    }

    /** TODO: For the future, the URL has to be changed and specified for getting matched users */
    @GetMapping("/users/matches")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActivityGetDTO> getActivitiesWIthMatchedUsers(@RequestHeader("Auth-Token")String token) {
        // checks if the visitor has a valid token
        userService.checkIfValidToken(token);

        // fetch all users in the internal representation
        //List<Activity> matches = userService.getUsers();
        List<UserGetDTOProfile> userGetDTOProfiles = new ArrayList<>();

        // convert each user to the API representation
        /**
         *

        for (User user : users) {
            userGetDTOProfiles.add(DTOMapperUser.INSTANCE.convertEntityToUserGetDTOProfile(user));
        }
        return userGetDTOProfiles;
         */
        return null;
    }

    @GetMapping("/users/profile/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUserProfile(@PathVariable Long userId, @RequestBody String verificationToken){

        throw new UnsupportedOperationException("Not implemented yet");

        // verifies Email
        //userService.verifyEmail(verificationToken);
    }
}
