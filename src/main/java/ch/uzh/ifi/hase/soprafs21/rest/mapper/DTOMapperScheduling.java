package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entities.ScheduledActivity;
import ch.uzh.ifi.hase.soprafs21.entities.SchedulingSession;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.ScheduledActivityPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.schedulingDTO.SchedulingSessionPutDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DTOMapperScheduling {

    DTOMapperScheduling INSTANCE = Mappers.getMapper(DTOMapperScheduling.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "date", target = "date")
    ScheduledActivityGetDTO convertEntityToScheduledActivityGetDTO(ScheduledActivity scheduledActivity);

    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "date", target = "date")
    ScheduledActivity convertScheduledActivityPostDTOToEntity(ScheduledActivityPostDTO scheduledActivityPostDTO);

    @Mapping(source = "chosenActivity", target = "chosenActivity")
    @Mapping(source = "activityList", target = "activityList")
    @Mapping(source = "chosenLocation", target = "chosenLocation")
    @Mapping(source = "locationList", target = "locationList")
    @Mapping(source = "chosenDate", target = "chosenDate")
    @Mapping(source = "dateList", target = "dateList")
    @Mapping(source = "offer", target = "offer")
    @Mapping(source = "answer", target = "answer")
    SchedulingSession convertSchedulingSessionPutDTOToEntity(SchedulingSessionPutDTO schedulingSessionPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "chosenActivity", target = "chosenActivity")
    @Mapping(source = "activityList", target = "activityList")
    @Mapping(source = "chosenLocation", target = "chosenLocation")
    @Mapping(source = "locationList", target = "locationList")
    @Mapping(source = "chosenDate", target = "chosenDate")
    @Mapping(source = "dateList", target = "dateList")
    @Mapping(source = "offer", target = "offer")
    @Mapping(source = "answer", target = "answer")
    SchedulingSessionGetDTO convertEntityToSchedulingSessionGetDTO(SchedulingSession schedulingSession);

    List<ScheduledActivityGetDTO> convertEntityListToScheduledActivityGetDTOList(List<ScheduledActivity> scheduledActivityList);
}