package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ScheduledActivity")
public class ScheduledActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Activity activity;

    @Column
    @NotBlank(message = "Must not be empty")
    private String location;

    @Column
    private LocalDateTime date;

    public ScheduledActivity(){ }

    public ScheduledActivity(Activity activity, String location, LocalDateTime date){
        this.activity = activity;
        this.location = location;
        this.date = date;
    }

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
