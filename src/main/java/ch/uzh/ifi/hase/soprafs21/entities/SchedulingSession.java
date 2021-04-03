package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;



@Entity
@Table(name = "SchedulingSession")
public class SchedulingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<UserActivity> activityList;

    @Column
    private UserActivity chosenActivity;

    @Column
    private List<String> locationList;

    @Column
    private String chosenLocation;

    @Column
    private List<Date> dateList;

    @Column
    private Date chosenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserActivity> getUserActivities() {
        return activityList;
    }

    public void setUserActivities(List<UserActivity> activityList) { this.activityList = activityList;}

    public UserActivity getChosenActivity() {
        return chosenActivity;
    }

    public void setChosenActivity(UserActivity chosenActivity) {
        this.chosenActivity = chosenActivity;
    }

    public List<String> getLocations() {
        return locationList;
    }

    public void setLocations(List<String> locationList) {
        this.locationList = locationList;
    }

    public String getChosenLocation() {
        return chosenLocation;
    }

    public void setChosenLocation(String chosenLocation) {
        this.chosenLocation = chosenLocation;
    }

    public List<Date> getDates() {
        return dateList;
    }

    public void setDates(List<Date> dateList) {
        this.dateList= dateList;
    }

    /**
     * ToDo
     */
    public void proposeLocation(String location){ }

    public void proposeDate(Date date){ }

    public void saveScheduledDate(){ }


}
