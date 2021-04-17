package ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO;

import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;

import java.util.List;

public class ActivityPutDTO {

    private Long id;
    private List<UserSwipeStatus> userSwipeStatusList;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public List<UserSwipeStatus> getUserSwipeStatusList() {return userSwipeStatusList;}

    public void setUserSwipeStatusList(List<UserSwipeStatus> userSwipeStatusList) {this.userSwipeStatusList = userSwipeStatusList;}
}
