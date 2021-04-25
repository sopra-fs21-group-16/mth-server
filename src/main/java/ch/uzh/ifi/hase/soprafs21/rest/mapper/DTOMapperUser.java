package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserGetDTOProfile;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO.UserPutDTO;
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
public interface DTOMapperUser {

    DTOMapperUser INSTANCE = Mappers.getMapper(DTOMapperUser.class);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "lastSeen", target = "lastSeen")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "userInterests", target = "userInterests")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "userInterests", target = "userInterests")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "lastSeen", target = "lastSeen")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "userInterests", target = "userInterests")
    UserGetDTOProfile convertEntityToUserGetDTOProfile(User user);



}
