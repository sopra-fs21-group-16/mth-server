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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String EmailPattern = "(^$|.+@(.+\\.)?(uzh\\.ch|ethz\\.ch))";

    private static final String PhonePattern = "(^$|(0|\\+41)[0-9]{9})";

    private static final Pattern patternEmail = Pattern.compile(EmailPattern);

    private static final Pattern patternPhone = Pattern.compile(PhonePattern);



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
        checkIfValidEmail(newUser.getEmail());

        checkIfUserExistsByEmail(newUser);

        newUser.setToken(UUID.randomUUID().toString());

        // round the LocalDateTime to Minutes using truncatedTo
        newUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        return newUser;
    }

    public void checkIfUserExistsByEmail(User userToBeChecked) {
        User userByEmail = userRepository.findByEmail(userToBeChecked.getEmail());

        String baseErrorMessage = "The %s provided %s not unique and already used. Please use another email!";
        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email", "is"));
        }
    }

    public String loginUser(User userInput) {
        User userByEmail = userRepository.findByEmail(userInput.getEmail());
        try {
            checkIfUserExistsByEmail(userByEmail);
        } catch (ResponseStatusException error) {
            if(userInput.getPassword().equals(userByEmail.getPassword())){
                userByEmail.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                userByEmail.setToken(UUID.randomUUID().toString());
                userRepository.save(userByEmail);
                userRepository.flush();
                return userByEmail.getToken();
            }
            else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is wrong.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username does not exist.");
    }

    public void logOutUser(long userId){
        User userById = userRepository.findById(userId);

        userById.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        userById.setToken(null);

        // saves the given entity but data is only persisted in the database once flush() is called
        userRepository.save(userById);
        userRepository.flush();
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided user could not be found.");
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

    /**
     * apply the change of the user data, throw error if only null values are given
     * @param userInput
     * @param userFromRepo
     */
    public void applyUserProfileChange(User userInput,User userFromRepo) {
        boolean noNewData = true;

        if(userInput.getEmail() != null){
            checkIfValidEmail(userInput.getEmail());
            userFromRepo.setEmail(userInput.getEmail());
            noNewData = false;
        }

        if (userInput.getPassword() != null){
            userFromRepo.setPassword(userInput.getPassword());
            noNewData = false;
        }

        if (userInput.getName() != null){
            userFromRepo.setName(userInput.getName());
            noNewData = false;
        }

        if (userInput.getBio() != null){
            userFromRepo.setBio(userInput.getBio());
            noNewData = false;
        }

        if (userInput.getPhone() != null){
            checkIfValidPhone(userInput.getPhone());
            userFromRepo.setPhone(userInput.getPhone());
            noNewData = false;
        }

        if (userInput.getGender() != null){
            userFromRepo.setGender(userInput.getGender());
            noNewData = false;
        }

        if (userInput.getProfilePicture() != null){
            userFromRepo.setProfilePicture(userInput.getProfilePicture());
            noNewData = false;
        }

        if (userInput.getUserInterests() != null){
            userFromRepo.setUserInterests(userInput.getUserInterests());
            noNewData = false;
        }

        else if(noNewData){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No change data was provided");
        }

        userRepository.save(userFromRepo);

        userRepository.flush();
    }

    public void checkIfUserExistsWithGivenId(long userId){
        try{
            userRepository.findById(userId);
        }
        catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("user with " + userId + " was not found"));
        }
    }

    public boolean checkIfValidToken(String tokenToCheck){
        boolean validStatus = false;

        // token is invalid if token is null
        if(tokenToCheck == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid");
        }

        // get a list of all registered users
        UserService userService = new UserService(userRepository);
        List<User> users = userService.getUsers();

        // and token is invalid if token is not consistent with a token inside the repo
        for (User user : users){
            if(tokenToCheck.equals(user.getToken())){
                validStatus = true;
            }
        }

        // throw exception if token is not consistent to any user in repo --> meaning that someone external tries to leak data
        if(!validStatus){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid, you have to be a user to have access");
        }

        return true;
    }

    /**
     * checks if a given email follows the allowed pattern
     * @param emailToCheck
     * @return
     */
    public boolean checkIfValidEmail(String emailToCheck){
        Matcher matcher = patternEmail.matcher(emailToCheck);
        if(!matcher.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is not valid, please use a UZH or ETH Zurich email");
        }
        return true;
    }

    public boolean checkIfValidPhone(String phoneToCheck){
        Matcher matcher = patternPhone.matcher(phoneToCheck);
        if(!matcher.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The phone number is not valid, must be a valid swiss phone number");
        }
        return true;
    }

}
