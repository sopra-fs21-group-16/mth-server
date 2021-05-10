package ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO;

import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userSwipeStatusDTO.UserSwipeStatusGetDTO;

import java.util.List;

public class ActivityGetDTO {

    private Long id;
    private ActivityPreset activityPreset;
    private List<UserSwipeStatusGetDTO> userSwipeStatusList;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public ActivityPreset getActivityPreset() {return activityPreset;}

    public void setActivityPreset(ActivityPreset activityPreset) {this.activityPreset = activityPreset;}

    public List<UserSwipeStatusGetDTO> getUserSwipeStatusList() {return userSwipeStatusList;}

    public void setUserSwipeStatusList(List<UserSwipeStatusGetDTO> userSwipeStatusList) {this.userSwipeStatusList = userSwipeStatusList;}
}
