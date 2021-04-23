package ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO;

import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;

import java.util.List;

public class ActivityGetDTO {

    private Long id;
    private ActivityPreset activityPreset;
    private List<UserSwipeStatus> userSwipeStatusList;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public ActivityPreset getActivityPreset() {return activityPreset;}

    public void setActivityPreset(ActivityPreset activityPreset) {this.activityPreset = activityPreset;}

    public List<UserSwipeStatus> getUserSwipeStatusList() {return userSwipeStatusList;}

    public void setUserSwipeStatusList(List<UserSwipeStatus> userSwipeStatusList) {this.userSwipeStatusList = userSwipeStatusList;}
}
