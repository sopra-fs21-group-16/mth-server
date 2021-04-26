package ch.uzh.ifi.hase.soprafs21.entities;

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

    @OneToOne
    private Activity proposerActivity;

    @OneToOne
    private Activity receiverActivity;

    @ElementCollection
    @CollectionTable(name = "SCHEDULING_SESSION_LOCATION_LIST", joinColumns = @JoinColumn(name = "locationId"))
    @Column
    private List<String> locationList;

    @Column
    private String chosenLocation;

    @Column
    private String proposerLocation;

    @Column
    private String receiverLocation;

    @ElementCollection
    @CollectionTable(name = "SCHEDULING_SESSION_DATE_LIST", joinColumns = @JoinColumn(name = "dateId"))
    @Column
    private List<LocalDateTime> dateList;

    @Column
    private LocalDateTime chosenDate;

    @Column
    private LocalDateTime proposerDate;

    @Column
    private LocalDateTime receiverDate;

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

    public void setChosenActivity(Activity chosenActivity) {this.chosenActivity = chosenActivity;}

    public List<String> getLocationList() {return locationList; }

    public void setLocationList(List<String> locationList) {this.locationList = locationList;}

    public String getChosenLocation() {return chosenLocation;}

    public void setChosenLocation(String chosenLocation) {this.chosenLocation = chosenLocation;}

    public List<LocalDateTime> getDateList() {return dateList;}

    public void setDateList(List<LocalDateTime> dateList) {this.dateList= dateList;}

    public LocalDateTime getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(LocalDateTime chosenDate) {
        this.chosenDate = chosenDate;
    }

    public void proposeLocation(String location) {
    }

    public void proposeDate(LocalDateTime date) {
    }

    public void saveScheduledDate() {
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


