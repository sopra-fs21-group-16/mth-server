package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is used to represent the activities that are used in the UserActivity
 */
@Entity
@Table(name = "ActivityPreset")
public class ActivityPreset implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String activityName;

    @Column(nullable = false)
    private ActivityCategory activityCategory;

    /**
     * googlePOI was left out, since we have to discuss how the relationship shall be between
     * the GooglePOI object and the activityPreset --> should one activity have multiple locations, since for example football can be played on multiple locations?
     private GooglePOI googlePOI;
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }
}
