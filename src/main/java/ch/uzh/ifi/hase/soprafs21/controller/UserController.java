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

    //Schedule - to get ScheduledActivity after a successful session
    @PostMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ScheduledActivity getScheduledActivity(@PathVariable Long sessionId, @RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the POST request will be successful
        userService.authorizationCheck(token);
        */


        return null;// Json object - scheduledActivity

    }

    //Schedule - To get all matched activities and start a scheduling session
    @GetMapping("/schedules/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Activity> getListOfActivitiesAndSessionID(@RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the GET request will be successful
        userService.authorizationCheck(token);
        */

        /*
        in UserService:
        if some paramaters are no longer true e.g. other user went offline etc.
        throw ResponseStatusException "410 GONE"
         */


        return null; //List<Activity> and sessionId via Header?

    }

    //Schedule - To get proposed locations/dates etc. during a Session
    @GetMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScheduledActivity getProposedInformation(@PathVariable Long sessionId, @RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the GET request will be successful
        userService.authorizationCheck(token);
        */


        return null; //json object  - updated SchedulingSession

    }

    //Schedule - To set proposed locations/dates etc. during a Session
    @PutMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void setProposedInformation(@PathVariable Long sessionId, @RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the PUT request will be successful
        userService.authorizationCheck(token);
        */

        /*
        in UserService:
        if trying to access a non-existing session
        throw ResponseStatusException "400 BAD REQUEST"
         */
    }

    //Schedule - To delete a scheduledActivity of User A and B
    @DeleteMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteScheduledActivity(@PathVariable Long sessionId, @RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the DELETE request will be successful
        userService.authorizationCheck(token);
         */


        //Update User information of A and B (?)
    }


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {

        throw new UnsupportedOperationException("Not implemented yet");

        // convert API user to internal representation
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        //User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        //return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody UserPostDTO userPostDTO){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks User input and returns token when successful
        //return userService.loginUser(userPostDTO);
    }

    @PostMapping("/users/{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logout(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        // checks if user id and token are from the same user
        //userService.authorizationCheck(userId, token);

        //return userService.logoutUser(userId);
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
