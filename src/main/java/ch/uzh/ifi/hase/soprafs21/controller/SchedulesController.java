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
 * This class is responsible for handling all REST request that are related to scheduling session.
 * The controller will receive the request and delegate the execution to the UserService and ScheduleService and finally return the result.
 */
@RestController
public class SchedulesController {

    private final UserService userService;

    SchedulesController(UserService userService) {
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

}
