package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate creationDate;

    // OneToOne since every UserActivity object has only one ActivityPreset
    // unidirectional relationship since ActivityPreset does not have to be aware of the UserActivity
    // cascade option enabled to map objects
    @OneToOne(cascade = {CascadeType.ALL})
    private ActivityPreset activityPreset;

    // since two UserSwipeStatus objects are needed per UserActivity
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<UserSwipeStatus> userSwipeStatusList;

    public Activity(){}

    public Activity(ActivityPreset activityPreset, List<UserSwipeStatus> userSwipeStatusList) {
        this.setActivityPreset(activityPreset);
        this.setUserSwipeStatusList(userSwipeStatusList);
        this.setCreationDate(LocalDate.now());
    }

    public Long getId() {return id;}

    public void setId(Long id) { this.id = id; }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Activity other = (Activity) obj;
        if ((this.getActivityPreset() == null) ? (other.getActivityPreset() != null) : !this.getActivityPreset().getActivityName().equals(other.getActivityPreset().getActivityName())) {
            return false;
        }

        if(this.getUserSwipeStatusList().size() != other.getUserSwipeStatusList().size()) {
            return false;
        }

        List<Long> userIdList = new ArrayList<>();
        for(UserSwipeStatus userSwipeStatus : this.getUserSwipeStatusList()) {
            userIdList.add(userSwipeStatus.getUser().getId());
        }
        for(UserSwipeStatus userSwipeStatus : other.getUserSwipeStatusList()) {
            if(!userIdList.contains(userSwipeStatus.getUser().getId())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (this.getActivityPreset() != null ? this.getActivityPreset().hashCode() : 0);
        hash = 31 * hash + (this.getUserSwipeStatusList() != null ? this.getUserSwipeStatusList().hashCode() : 0);
        return hash;
    }
}