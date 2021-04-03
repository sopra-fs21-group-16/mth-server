package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * This class is used to save information about activities and its associated user decisions
 */
@Entity
@Table(name = "UserActivity")
public class UserActivity implements Serializable{

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

    // since two UserDecisionStatus objects are needed per UserActivity
    @OneToMany
    private List<UserDecisionStatus> userDecisionStatusList;

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

    public List<UserDecisionStatus> getUserDecisionStatusList() {
        return userDecisionStatusList;
    }

    public void setUserDecisionStatusList(List<UserDecisionStatus> userDecisionStatusList) {
        this.userDecisionStatusList = userDecisionStatusList;
    }

    /**
     * ToDo
     */
    public Boolean isMatch(){
        return null;
    }
}
