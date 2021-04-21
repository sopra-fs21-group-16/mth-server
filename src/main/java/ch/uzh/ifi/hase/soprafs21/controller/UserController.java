package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOProfile;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperUser;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
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
    public String login(@RequestBody UserPostDTO userPostDTO){
        // convert API user to internal representation
        User userInput = DTOMapperUser.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

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

    @PutMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUserProfile(@RequestBody UserPutDTO userPutDTOProfile, @PathVariable Long userId, @RequestHeader("Auth-Token")String token){
        // convert API user to internal representation
        User userInput = DTOMapperUser.INSTANCE.convertUserPutDTOtoEntity(userPutDTOProfile);

        // Checks if the provided user exists and whether the token is valid and matches the user --> if they match, then user is on own profile
        userService.isUserAuthenticated(userId,token);

        // find the user in the repo that holds resource to be changed
        User userFromRepo = userService.getUserByID(userId);

        // Checks whether the user is authenticated and authorized to use a resource, given the resource owner userId
        userService.isUserAuthorized(userId, userFromRepo.getId(), token);

        // creates user profile
        userService.applyUserProfileChange(userInput,userFromRepo);
    }

    @GetMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.OK)
    public Object getUserProfile(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        // checks if the profile to be visited exists
        userService.checkIfUserExistsWithGivenId(userId);

        // checks if the visitor has a valid token
        userService.checkIfValidToken(token);

        // get user profile
        User userFromRepo = userService.getUserByID(userId);

        // if user is on his own profile, he can see additional data like email --> return UserGetDTO
        try{
            if(userService.isUserAuthenticated(userId,token)){
                return DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(userFromRepo);
            }
        }catch(ResponseStatusException ignored){}

        // if user is on others profile, he can only see public data --> return UserGetDTOProfile
        return DTOMapperUser.INSTANCE.convertEntityToUserGetDTOProfile(userFromRepo);
    }

    /** TODO: For the future, the URL has to be changed and specified for getting matched users */
    @GetMapping("/users/profiles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTOProfile> getAllProfiles(@RequestHeader("Auth-Token")String token) {
        // checks if the visitor has a valid token
        userService.checkIfValidToken(token);

        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTOProfile> userGetDTOProfiles = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOProfiles.add(DTOMapperUser.INSTANCE.convertEntityToUserGetDTOProfile(user));
        }
        return userGetDTOProfiles;
    }

    @GetMapping("/users/{userId}/profile/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUserProfile(@PathVariable Long userId, @RequestBody String verificationToken){

        throw new UnsupportedOperationException("Not implemented yet");

        // verifies Email
        //userService.verifyEmail(verificationToken);
    }
}
