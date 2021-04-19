package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;

import java.util.List;

public class ActivityGetDTO {
    private List<UserSwipeStatus> userSwipeStatusList;
    private ActivityPreset activityPreset;

    public List<UserSwipeStatus> getUserSwipeStatusList() { return userSwipeStatusList; }

    public void setUserSwipeStatusList(List<UserSwipeStatus> userSwipeStatusList) { this.userSwipeStatusList = userSwipeStatusList; }

    public ActivityPreset getActivityPreset() { return activityPreset; }

    public void setActivityPreset(ActivityPreset activityPreset) { this.activityPreset = activityPreset; }
}
