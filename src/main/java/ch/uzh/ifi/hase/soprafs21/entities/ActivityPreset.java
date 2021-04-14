package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.ExternalAPI.GooglePOI;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityCategory activityCategory;

    //@OneToOne
    //private GooglePOI googlePOI;

    public ActivityPreset(){

    }

    ActivityPreset(String activityName, ActivityCategory activityCategory){
        this.activityName = activityName;
        this.activityCategory = activityCategory;
    }

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

    /**
    public GooglePOI getGooglePOI() {
        return googlePOI;
    }

    public void setGooglePOI(GooglePOI googlePOI) {
        this.googlePOI = googlePOI;
    }
 */
}
