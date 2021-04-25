package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entities.Activity;
import ch.uzh.ifi.hase.soprafs21.rest.dto.activityDTO.ActivityGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapperActivity {

    DTOMapperActivity INSTANCE = Mappers.getMapper(DTOMapperActivity.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "activityPreset", target = "activityPreset")
    @Mapping(source = "userSwipeStatusList", target = "userSwipeStatusList")
    ActivityGetDTO convertEntityToActivityGetDTO(Activity activity);
}
