package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "SchedulingSession")
public class SchedulingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Activity> activityList;

    @OneToOne
    private Activity chosenActivity;

    @ElementCollection
    @CollectionTable(name="SCHEDULING_SESSION_LOCATION_LIST", joinColumns = @JoinColumn(name = "locationId"))
    @Column
    private List<String> locationList;

    @Column
    private String chosenLocation;

    @ElementCollection
    @CollectionTable(name="SCHEDULING_SESSION_DATE_LIST", joinColumns = @JoinColumn(name = "dateId"))
    @Column
    private List<LocalDateTime> dateList;

    @Column
    private LocalDateTime chosenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Activity> getUserActivities() {
        return activityList;
    }

    public void setUserActivities(List<Activity> activityList) { this.activityList = activityList;}

    public Activity getChosenActivity() {
        return chosenActivity;
    }

    public void setChosenActivity(Activity chosenActivity) {
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

    public List<LocalDateTime> getDates() {
        return dateList;
    }

    public void setDates(List<LocalDateTime> dateList) {
        this.dateList= dateList;
    }

    public LocalDateTime getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(LocalDateTime chosenDate) { this.chosenDate = chosenDate; }

    /**
     * ToDo
     */
    public void proposeLocation(String location){ }

    public void proposeDate(LocalDateTime date){ }

    public void saveScheduledDate(){ }


}
