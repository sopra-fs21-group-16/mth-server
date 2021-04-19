package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.util.Date;
import java.util.List;


/**
 * This class is used to save information about activities and its associated user decisions
 */
@Entity
@Table(name = "Activity")
public class Activity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date creationDate;

    // OneToOne since every UserActivity object has only one ActivityPreset
    // unidirectional relationship since ActivityPreset does not have to be aware of the UserActivity
    @OneToOne
    private ActivityPreset activityPreset;

    // since two UserSwipeStatus objects are needed per UserActivity
    @OneToMany
    private List<UserSwipeStatus> userSwipeStatusList;

    public Activity(){}

    public Activity(ActivityPreset activityPreset, List<UserSwipeStatus> userSwipeStatusList) {
        this.setActivityPreset(activityPreset);
        this.setUserSwipeStatusList(userSwipeStatusList);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ActivityPreset getActivityPreset() {
        return activityPreset;
    }

    public void setActivityPreset(ActivityPreset activityPreset) {
        this.activityPreset = activityPreset;
    }

    public List<UserSwipeStatus> getUserSwipeStatusList() {
        return userSwipeStatusList;
    }

    public void setUserSwipeStatusList(List<UserSwipeStatus> userSwipeStatusList) {
        this.userSwipeStatusList = userSwipeStatusList;
    }

    /**
     * ToDo
     */
    public Boolean isMatch(){
        return null;
    }
}
