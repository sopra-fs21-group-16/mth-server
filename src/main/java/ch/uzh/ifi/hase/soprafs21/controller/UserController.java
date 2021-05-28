package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperActivity;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperUser;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    private final ActivityService activityService;

    UserController(UserService userService, ActivityService activityService) {
        this.userService = userService;
        this.activityService = activityService;
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

    @GetMapping("/users/matches")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActivityGetDTO> getActivitiesWithMatchedUsers(@RequestHeader("Auth-Token")String token) {
        // checks if the visitor has a valid token
        userService.checkIfValidToken(token);

        // get user profile
        User userFromRepo = userService.getUserByToken(token);


        List<Activity> activityList = activityService.getAllActivitiesWithMatchedUsers(userFromRepo);

        List<ActivityGetDTO> activityGetDTOs = new ArrayList<>();
        for (Activity activity : activityList) {
            activityGetDTOs.add(DTOMapperActivity.INSTANCE.convertEntityToActivityGetDTO(activity));
        }

        return activityGetDTOs;
    }
}
