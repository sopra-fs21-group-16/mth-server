package ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;

import java.time.LocalDateTime;
import java.util.List;

public class SchedulingSessionGetDTO {

    private Long id;
    private List<Activity> activityList;
    private Activity chosenActivity;
    private List<String> locationList;
    private String chosenLocation;
    private List<LocalDateTime> dateList;
    private LocalDateTime chosenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Activity getChosenActivity() {
        return chosenActivity;
    }

    public void setChosenActivity(Activity chosenActivity) {
        this.chosenActivity = chosenActivity;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    public String getChosenLocation() {
        return chosenLocation;
    }

    public void setChosenLocation(String chosenLocation) {
        this.chosenLocation = chosenLocation;
    }

    public List<LocalDateTime> getDateList() {
        return dateList;
    }

    public void setDateList(List<LocalDateTime> dateList) {
        this.dateList = dateList;
    }

    public LocalDateTime getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(LocalDateTime chosenDate) {
        this.chosenDate = chosenDate;
    }

}
