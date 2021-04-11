package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

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
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToCheck
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToCheck) {
        User userByUsername = userRepository.findByEmail(userToCheck.getEmail());

        String baseErrorMessage = "The %s provided %s is already taken.";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username ", "is"));
        }
    }

    public String loginUser(UserPostDTO userPostDTO) {
        User userByUsername = userRepository.findByEmail(userPostDTO.getEmail());
        try {
            checkIfUserExists(userByUsername);
        } catch (ResponseStatusException error) {
            if(userPostDTO.getPassword().equals(userByUsername.getPassword())){
                userByUsername.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                userByUsername.setToken(UUID.randomUUID().toString());
                return userByUsername.getToken();

            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is wrong.");
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does not exist.");
    }

}
