package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
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

    public User getUserByID(long userId){
        User userById = this.userRepository.findById(userId);
        return userById;
    }

    public User createUser(User newUser) {
        checkIfUserExists(newUser);

        newUser.setToken(UUID.randomUUID().toString());

        // round the LocalDateTime to Minutes using truncatedTo
        newUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        return newUser;
    }

    public void checkIfUserExists(User userToBeCreated) {
        User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

        String baseErrorMessage = "The %s provided %s not unique and already used. Please use another email!";
        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "email", "is"));
        }
    }


    public void logOutUser(long userId){
        User userById = userRepository.findById(userId);

        userById.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        userById.setToken(null);

        // saves the given entity but data is only persisted in the database once flush() is called
        userRepository.save(userById);
        userRepository.flush();
    }

}
