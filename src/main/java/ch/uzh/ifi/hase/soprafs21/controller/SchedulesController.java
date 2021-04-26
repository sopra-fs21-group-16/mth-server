package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperScheduling;
import ch.uzh.ifi.hase.soprafs21.service.SchedulingService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to scheduling session.
 * The controller will receive the request and delegate the execution to the UserService and ScheduleService and finally return the result.
 */
@RestController
public class SchedulesController {

    private final UserService userService;

    private final SchedulingService schedulingService;


    SchedulesController(UserService userService, SchedulingService schedulingService) {
        this.userService = userService;
        this.schedulingService = schedulingService;
    }

    //Schedule - to get ScheduledActivity after a successful session
    @PostMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ScheduledActivity getScheduledActivity(@PathVariable Long sessionId, @RequestHeader("Auth-Token") String token) {
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
    public SchedulingSessionGetDTO getProposedInformation(@PathVariable Long sessionId, @RequestHeader("Auth-Token") String token) {

        userService.checkIfValidToken(token);

        SchedulingSession schedulingSession = schedulingService.getSchedulingSession(sessionId, token);

        return DTOMapperScheduling.INSTANCE.convertEntityToSchedulingSessionGetDTO(schedulingSession);
    }

    //Schedule - To set proposed locations/dates etc. during a Session
    @PutMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void setProposedInformation(@PathVariable Long sessionId, @PathVariable SchedulingSessionPutDTO schedulingSessionPutDTO, @RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);

        SchedulingSession schedulingSession = DTOMapperScheduling.INSTANCE.convertSchedulingSessionPutDTOToEntity(schedulingSessionPutDTO);

        schedulingService.updateSchedulingSession(sessionId, schedulingSession, token);
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
