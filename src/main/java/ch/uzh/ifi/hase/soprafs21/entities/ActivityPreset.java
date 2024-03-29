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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityCategory activityCategory;

    @Column
    private String googlePOICategory;

    @Column
    private String googlePOIKeyword;

    @Column
    private String imageURL;

    public ActivityPreset(){}

    public ActivityPreset(String activityName, ActivityCategory activityCategory){
        this.activityName = activityName;
        this.activityCategory = activityCategory;
    }

    public ActivityPreset(String activityName, ActivityCategory activityCategory, String googlePOICategory, String googlePOIKeyword, String imageURL){
        this.activityName = activityName;
        this.activityCategory = activityCategory;
        this.googlePOICategory = googlePOICategory;
        this.googlePOIKeyword = googlePOIKeyword;
        this.imageURL = imageURL;

    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getActivityName() {return activityName;}

    public void setActivityName(String activityName) {this.activityName = activityName;}

    public ActivityCategory getActivityCategory() {return activityCategory;}

    public void setActivityCategory(ActivityCategory activityCategory) {this.activityCategory = activityCategory;}

    public String getGooglePOICategory() {return googlePOICategory;}

    public void setGooglePOICategory(String googlePOICategory) {this.googlePOICategory = googlePOICategory;}

    public String getGooglePOIKeyword() {return googlePOIKeyword;}

    public void setGooglePOIKeyword(String googlePOIKeyword) {this.googlePOIKeyword = googlePOIKeyword;}

    public String getImageURL() { return imageURL; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
