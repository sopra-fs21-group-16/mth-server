package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.DecisionStatus;

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

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @Column
    private DecisionStatus decisionStatus;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DecisionStatus getDecisionStatus() {
        return decisionStatus;
    }

    public void setDecisionStatus(DecisionStatus decisionStatus) {
        this.decisionStatus = decisionStatus;
    }
}
