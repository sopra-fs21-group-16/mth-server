package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ScheduledActivity")
public class ScheduledActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private UserActivity chosenActivity;

    @Column
    private String chosenLocation;

    @Column
    private Date chosenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserActivity getChosenActivity() {
        return chosenActivity;
    }

    public void setChosenActivity(UserActivity chosenActivity) {
        this.chosenActivity = chosenActivity;
    }

    public String getChosenLocation() {
        return chosenLocation;
    }

    public void setChosenLocation(String chosenLocation) {
        this.chosenLocation = chosenLocation;
    }

    public Date getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(Date chosenDate) {
        this.chosenDate = chosenDate;
    }

}

