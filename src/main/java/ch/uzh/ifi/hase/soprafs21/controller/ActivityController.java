package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperActivity;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Activity Controller
 */
@RestController
public class ActivityController {

    private final ActivityService activityService;

    ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityGetDTO> getActivities(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){
        List<Activity> activitiesToSwipe = activityService.getActivities(userId, token);
        return DTOMapperActivity.INSTANCE.convertEntityListToActivityGetDTOList(activitiesToSwipe);
    }

    @PutMapping("/activities/swipe/{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean updateActivity(@RequestBody SwipeStatus swipeStatus, @PathVariable Long userId, @PathVariable Long activityId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

    }

}
