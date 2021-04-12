package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        /* ToDo */
        return null;
    }

    /**
     * Checks if the provided user exists and whether the token is valid and matches the user
     * @param userId user who gets authenticated
     * @param token Auth-Token
     * @return true if authenticated
     */
    public boolean isUserAuthenticated(long userId, String token) {
        User userById = userRepository.findById(userId);

        if (userById == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided user could not be found.");
        } else if (!userById.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provided user does not match Auth-Token.");
        }

        return true;
    }

    /**
     * Checks whether the user is authenticated and authorized to use a resource, given the resource owner userId
     * @param userId user who requests authorization
     * @param ownerUserId resource owner
     * @param token Auth-Token
     * @return true if authorized
     */
    public boolean isUserAuthorized(long userId, long ownerUserId, String token) {
        isUserAuthenticated(userId, token);

        if(userId != ownerUserId){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized to access requested resource");
        }

        return true;
    }



}
