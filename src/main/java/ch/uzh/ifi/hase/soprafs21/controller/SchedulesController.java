package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperScheduling;
import ch.uzh.ifi.hase.soprafs21.service.SchedulingService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Schedules Controller
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

    //Schedule - to get IDs of all Scheduling Sessions with this user
    @GetMapping("/schedules")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> getSchedulingSessionOfUser(@RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);

        return schedulingService.getSchedulingSessionsOfUser(token);
    }


    //Schedule - to crate Scheduling Session
    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SchedulingSessionGetDTO createSchedulingSession(@RequestBody UserIdsSchedulingPostDTO userIdsSchedulingPostDTO, @RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);

        SchedulingSession schedulingSession = schedulingService.createSchedulingSession(userIdsSchedulingPostDTO.getUserId1(), userIdsSchedulingPostDTO.getUserId2(), userIdsSchedulingPostDTO.getOffer(), token);
        return DTOMapperScheduling.INSTANCE.convertEntityToSchedulingSessionGetDTO(schedulingSession);
    }

    /* // old version and not a good solution to crate scheduling session
    Schedule - To get all matched activities and start a scheduling session
    @GetMapping("/schedules/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Activity> getListOfActivitiesAndSessionID(@RequestHeader("Auth-Token")String token) {
        /*
        only if the sent token is in the rep, the GET request will be successful
        userService.authorizationCheck(token);

        in UserService:
        if some parameters are no longer true e.g. other user went offline etc.
        throw ResponseStatusException "410 GONE"
         *
        return null; //List<Activity> and sessionId via Header?
    }
    */

    //Schedule - to get ScheduledActivity after a successful session
    @PostMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ScheduledActivityGetDTO getScheduledActivity(@PathVariable Long sessionId, @RequestBody ScheduledActivityPostDTO scheduledActivityPostDTO, @RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);
        ScheduledActivity scheduledActivity = DTOMapperScheduling.INSTANCE.convertScheduledActivityPostDTOToEntity(scheduledActivityPostDTO);
        return DTOMapperScheduling.INSTANCE.convertEntityToScheduledActivityGetDTO(schedulingService.saveScheduledActivity(sessionId, scheduledActivity));
    }

    @GetMapping("/schedules/activities/{scheduledActivityId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScheduledActivityGetDTO getSpecificScheduledActivity(@PathVariable Long scheduledActivityId, @RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);
        return DTOMapperScheduling.INSTANCE.convertEntityToScheduledActivityGetDTO(schedulingService.getSpecificScheduledActivity(scheduledActivityId, token));
    }

    @GetMapping("/schedules/activities")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScheduledActivityGetDTO> getSpecificScheduledActivity(@RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);
        return DTOMapperScheduling.INSTANCE.convertEntityListToScheduledActivityGetDTOList(schedulingService.getAllScheduledActivities(token));
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
    public void setProposedInformation(@PathVariable Long sessionId, @RequestBody SchedulingSessionPutDTO schedulingSessionPutDTO, @RequestHeader("Auth-Token") String token) {
        userService.checkIfValidToken(token);

        SchedulingSession schedulingSession = DTOMapperScheduling.INSTANCE.convertSchedulingSessionPutDTOToEntity(schedulingSessionPutDTO);

        schedulingService.updateSchedulingSession(sessionId, schedulingSession, token);
    }

    //Schedule - To delete a scheduledActivity of User A and B
    @DeleteMapping("/schedules/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteScheduledSession(@PathVariable Long sessionId, @RequestHeader("Auth-Token")String token) {

        // only if the sent token is in the rep, the DELETE request will be successful
        userService.checkIfValidToken(token);

        // check if session is valid
        schedulingService.checkIfScheduledSessionExistsWithGivenId(sessionId);

        // delete the session
        /* ToDo: Send token to deleteScheduledSession to check if sessionId fits token */
        schedulingService.deleteScheduledSession(sessionId);

        /** TODO: Update User information of A and B (?) */
    }
}
