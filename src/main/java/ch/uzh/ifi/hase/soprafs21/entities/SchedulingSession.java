package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "SchedulingSession")
public class SchedulingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @JoinColumn(name = "activity_id")
    private Set<Activity> activityList;

    @OneToOne
    private Activity chosenActivity;

    @Column
    private List<String> locationList;

    @Column
    private String chosenLocation;

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

    public Set<Activity> getUserActivities() {
        return activityList;
    }

    public void setUserActivities(Set<Activity> activityList) { this.activityList = activityList;}

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

    public void addLocation(String location){
        locationList.add(location);
    }

    public void removeLocation(String location){
        locationList.remove(location);
    }

    public void addDate(LocalDateTime date){
        dateList.add(date);
    }

    public void removeDate(LocalDateTime date){
        dateList.remove(date);
    }

    /**
     * ToDo
     */
    public void saveScheduledDate(){ }
}
