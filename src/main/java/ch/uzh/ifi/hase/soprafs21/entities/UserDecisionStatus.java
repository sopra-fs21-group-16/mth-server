package ch.uzh.ifi.hase.soprafs21.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is used to define if a user has accepted or discarded a proposed activity from the matching algorithm
 * --> has to be an entity in a table otherwise definition of relationship of UserActivity to UserDecisionStatus not possible
 */
@Entity
@Table(name = "UserDecisionStatus")
public class UserDecisionStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    // not sure if ID is needed --> maybe needed because every entity in a table needs an ID
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @Column
    private Boolean decisionStatus;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getDecisionStatus() {
        return decisionStatus;
    }

    public void setDecisionStatus(Boolean decisionStatus) {
        this.decisionStatus = decisionStatus;
    }
}
