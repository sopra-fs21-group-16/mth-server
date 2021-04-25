package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.entities.Activity;
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
    public List<Activity> getActivities(@PathVariable Long userId, @RequestHeader("Auth-Token")String token){

        throw new UnsupportedOperationException("Not implemented yet");

        /* ToDo: Get (& generate) activities to display them to the User. */
    }

    @PutMapping("/activities/swipe/{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSwipeStatus(@RequestBody SwipeStatus swipeStatus, @PathVariable Long activityId, @RequestHeader("Auth-Token")String token){

        activityService.setSwipingStatus(activityId,token,swipeStatus);
    }

}
