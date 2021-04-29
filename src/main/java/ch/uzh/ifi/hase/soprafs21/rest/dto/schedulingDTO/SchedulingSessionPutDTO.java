package ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;

import java.time.LocalDateTime;
import java.util.List;

public class SchedulingSessionPutDTO {

    private List<Activity> activityList;
    private Activity chosenActivity;
    private Activity proposerActivity;
    private Activity receiverActivity;
    private List<String> locationList;
    private String chosenLocation;
    private String proposerLocation;
    private String receiverLocation;
    private List<LocalDateTime> dateList;
    private LocalDateTime chosenDate;
    private LocalDateTime proposerDate;
    private LocalDateTime receiverDate;

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

    public void setLocationList(List<String> locationList) {this.locationList = locationList;}

    public String getChosenLocation() {return chosenLocation;}

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

    public Activity getProposerActivity() {
        return proposerActivity;
    }

    public void setProposerActivity(Activity proposerActivity) {
        this.proposerActivity = proposerActivity;
    }

    public Activity getReceiverActivity() {
        return receiverActivity;
    }

    public void setReceiverActivity(Activity receiverActivity) {
        this.receiverActivity = receiverActivity;
    }

    public String getProposerLocation() {
        return proposerLocation;
    }

    public void setProposerLocation(String proposerLocation) {
        this.proposerLocation = proposerLocation;
    }

    public String getReceiverLocation() {
        return receiverLocation;
    }

    public void setReceiverLocation(String receiverLocation) {
        this.receiverLocation = receiverLocation;
    }

    public LocalDateTime getProposerDate() {
        return proposerDate;
    }

    public void setProposerDate(LocalDateTime proposerDate) {
        this.proposerDate = proposerDate;
    }

    public LocalDateTime getReceiverDate() {
        return receiverDate;
    }

    public void setReceiverDate(LocalDateTime receiverDate) {
        this.receiverDate = receiverDate;
    }
}