package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.service.ActivityService;
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
    public List<Activity> getActivities(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        /* ToDo: Get (& generate) activities to display them to the User. */
    }

    @PutMapping("/activities/{userId}/swipe/{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@RequestBody SwipeStatus swipeStatus, @PathVariable Long userId, @PathVariable Long activityId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

    }

}
