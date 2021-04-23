package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public class ActivityPutDTO {
    private List<UserSwipeStatus> userSwipeStatusList;

    public List<UserSwipeStatus> getUserSwipeStatusList() { return userSwipeStatusList; }

    public void setUserSwipeStatusList(List<UserSwipeStatus> userSwipeStatusList) { this.userSwipeStatusList = userSwipeStatusList; }
}
