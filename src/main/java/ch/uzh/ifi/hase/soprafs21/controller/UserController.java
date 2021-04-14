package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

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
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody UserPostDTO userPostDTO){
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // login user and return token as String
        return userService.loginUser(userInput);
    }

    @PostMapping("/users/{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logout(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        // checks if user id and token are from the same user
        userService.isUserAuthenticated(userId, token);

        userService.logOutUser(userId);
    }

    @GetMapping("/users/{userId}/overview")
    @ResponseStatus(HttpStatus.OK)
    public List<Activity> getUserOverview(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        // returns all matches and activities in the form of a list of Activities
        //userService.getUserOverview(userId);
    }

    @GetMapping("/users/{userId}/ping")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userPing(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        // additional step necessary

    }

    @PostMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.OK)
    public void createUserProfile(@RequestBody User userProfile, @PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");


        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        // creates user profile
        //userService.createUserProfile(userProfile, userId);
    }

    @PutMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@RequestBody User profileUpdates, @PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        // updates user profile
        //userService.updateUserProfile(profileUpdates, userId);
    }

    @GetMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.OK)
    public void getUserProfile(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks only the token, because everyone can view a profile
        //userService.authorizationCheckOnlyToken(token);

        // returns user profile
        //userService.getUserProfile(userId);
    }

    @GetMapping("/users/{userId}/profile/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUserProfile(@PathVariable Long userId, @RequestBody String verificationToken){

        throw new UnsupportedOperationException("Not implemented yet");

        // verifies Email
        //userService.verifyEmail(verificationToken);
    }

}
