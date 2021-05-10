package ch.uzh.ifi.hase.soprafs21.rest.dto.userSwipeStatusDTO;

import ch.uzh.ifi.hase.soprafs21.constant.SwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOPublic;

public class UserSwipeStatusGetDTO {

    private Long id;
    private UserGetDTOPublic user;
    private SwipeStatus swipeStatus;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public UserGetDTOPublic getUser() {return user;}

    public void setUser(UserGetDTOPublic userGetDTOPublic) {this.user = userGetDTOPublic;}

    public SwipeStatus getSwipeStatus() {return swipeStatus;}

    public void setSwipeStatus(SwipeStatus swipeStatus) {this.swipeStatus = swipeStatus;}
}
