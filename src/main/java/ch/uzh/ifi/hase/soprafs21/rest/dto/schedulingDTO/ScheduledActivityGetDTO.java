package ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;

import java.time.LocalDateTime;

public class ScheduledActivityGetDTO {

    private Long id;
    private Activity activity;
    private String location;
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
