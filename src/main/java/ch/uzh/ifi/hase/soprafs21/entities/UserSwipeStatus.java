package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is used to define if a user has accepted or discarded a proposed activity from the matching algorithm
 * --> has to be an entity in a table otherwise definition of relationship of UserActivity to UserDecisionStatus not possible
 */
@Entity
@Table(name = "UserSwipeStatus")
public class UserSwipeStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @Column
    private SwipeStatus swipeStatus;

    public UserSwipeStatus(){}

    public UserSwipeStatus(User user, SwipeStatus swipeStatus){
        this.user = user;
        this.swipeStatus = swipeStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SwipeStatus getSwipeStatus() {
        return swipeStatus;
    }

    public void setSwipeStatus(SwipeStatus swipeStatus) {
        this.swipeStatus = swipeStatus;
    }
}
