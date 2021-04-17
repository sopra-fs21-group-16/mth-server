package ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO;

import ch.uzh.ifi.hase.soprafs21.entities.ActivityPreset;

public class ActivityGetDTO {

    private Long id;
    private ActivityPreset activityPreset;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public ActivityPreset getActivityPreset() {return activityPreset;}

    public void setActivityPreset(ActivityPreset activityPreset) {this.activityPreset = activityPreset;}
}
