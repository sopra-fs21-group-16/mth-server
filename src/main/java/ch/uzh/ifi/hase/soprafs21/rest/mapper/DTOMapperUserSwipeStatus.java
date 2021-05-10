package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entities.UserSwipeStatus;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userSwipeStatusDTO.UserSwipeStatusGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DTOMapperUserSwipeStatus {

    DTOMapperUserSwipeStatus INSTANCE = Mappers.getMapper(DTOMapperUserSwipeStatus.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "swipeStatus", target = "swipeStatus")
    UserSwipeStatusGetDTO convertEntityToUserSwipeStatusGetDTO(UserSwipeStatus userSwipeStatus);

    List<UserSwipeStatusGetDTO> convertEntityListToUserSwipeStatusGetDTOList(List<UserSwipeStatus> userSwipeStatusList);
}
