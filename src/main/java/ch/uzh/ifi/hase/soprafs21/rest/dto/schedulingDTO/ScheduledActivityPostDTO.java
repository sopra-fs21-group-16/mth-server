package ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import java.time.LocalDateTime;

public class ScheduledActivityPostDTO {

    private Activity activity;
    private String location;
    private LocalDateTime date;

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
