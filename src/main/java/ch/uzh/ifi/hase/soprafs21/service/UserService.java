package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.emailAuthentication.VerificationToken;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

    private final VerificationTokenRepository verificationTokenRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("verificationTokenRepository") VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    /**
     * Returns all users from UserRepository
     * @return
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserByID(long userId) {
        return this.userRepository.findById(userId);
    }

    public User getUserByToken(String token) {
        User userByToken = this.userRepository.findByToken(token);
        return userByToken;
    }

    public long getIdByToken(String token){
        User userByToken = this.userRepository.findByToken(token);
        return userByToken.getId();
    }

    public User createUser(User newUser) {

        checkIfUserExistsByEmail(newUser);

        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.setToken(UUID.randomUUID().toString());

        // round the LocalDateTime to Minutes using truncatedTo
        newUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        // saves the given entity but data is only persisted in the database once flush() is called
        try { // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush(); }
        catch (ConstraintViolationException ex) { handleValidationError(ex);}

        return newUser;
    }

    public void checkIfUserExistsByEmail(User userToBeChecked) {
        User userByEmail = userRepository.findByEmail(userToBeChecked.getEmail());

        String baseErrorMessage = "The %s provided %s not unique and already used. Please use another email!";
        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email", "is"));
        }
    }

    public User loginUser(User userInput) {
        User userByEmail = userRepository.findByEmail(userInput.getEmail());
        try {
            checkIfUserExistsByEmail(userByEmail);
        }

        catch (ResponseStatusException error) {
            if (bCryptPasswordEncoder.matches(userInput.getPassword(),userByEmail.getPassword())){
                checkIfEmailVerified(userByEmail);
                userByEmail.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                userByEmail.setToken(UUID.randomUUID().toString());
                userRepository.save(userByEmail);
                userRepository.flush();
                return userByEmail;
            }
            else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password is wrong.");
            }
        }
        catch (NullPointerException error) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password is wrong.");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Something went wrong.");
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
            userFromRepo.setEmail(userInput.getEmail());
            noNewData = false;
        }

        if (userInput.getPassword() != null){
            /** TODO: send emailVerification when changing password */
            userFromRepo.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            noNewData = false;
        }

        if (userInput.getName() != null){
            userFromRepo.setName(userInput.getName());
            noNewData = false;
        }

        if (userInput.getDateOfBirth() != null){
            userFromRepo.setDateOfBirth(userInput.getDateOfBirth());
            noNewData = false;
        }

        if (userInput.getBio() != null){
            userFromRepo.setBio(userInput.getBio());
            noNewData = false;
        }

        if (userInput.getPhone() != null){
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
            // if user has not yet a userInterests object, set a new one
            if(userFromRepo.getUserInterests() == null){
                userFromRepo.setUserInterests(userInput.getUserInterests());
            }
            // if user has already a userInterests object, update the userInterests object
            else {
                userFromRepo.getUserInterests().updateUserInterests(userInput.getUserInterests());
            }
            noNewData = false;
        }

        else if(noNewData){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No change data was provided");
        }

        try { // saves the given entity but data is only persisted in the database once flush() is called
            userRepository.save(userFromRepo);
            userRepository.flush(); }
        catch (ConstraintViolationException ex) { handleValidationError(ex);}
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

        // token is invalid if token is null
        if(tokenToCheck == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid");
        }

        // and token is invalid if token is not consistent with a token inside the repo
        // throw exception if token is not consistent to any user in repo
        if(userRepository.findByToken(tokenToCheck) == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid, you have to be a user to have access");
        }

        return true;
    }

    /**
     * to handle errors that occur when violating database constraints
     * @param ex
     */
    private void handleValidationError(ConstraintViolationException ex){
        AtomicReference exceptions = new AtomicReference<>();

        exceptions.set("Validation failed: \n ");
        ex.getConstraintViolations().forEach(violation -> { exceptions.set(exceptions + violation.getMessage() + "\n "); });

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exceptions.toString());
    }

    /**
     * convert is needed for the age attribute of the user
     * @param dateOfBirth
     * @return
     */
    public int computeAge(LocalDate dateOfBirth){
        // if no date of birth is set yet, we return 0
        if (dateOfBirth == null ){
            return 0;
        }

        // convert extracted data and get local data
        LocalDate localDateOfDateOfBirth = dateOfBirth;
        LocalDate now = LocalDate.now();
        Period differenceOfDates = Period.between(localDateOfDateOfBirth,now);

        // compute age
        int age = differenceOfDates.getYears();

        return age;
    }

    public void confirmRegistration(VerificationToken verificationToken){

        User user = verificationToken.getUser();

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public boolean checkIfEmailVerified(User user){
        if(!(user.getEmailVerified())){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "The email of the user is still not verified");
        }
        return true;
    }

    public VerificationToken getVerificationToken(String verificationToken){
        return verificationTokenRepository.findByToken(verificationToken);
    }

    public VerificationToken createVerificationToken(User user, String token){
        VerificationToken myToken = new VerificationToken(token,user);

        LocalDateTime expiryDate = myToken.calculateExpiryDate();
        myToken.setExpiryDate(expiryDate);

        return verificationTokenRepository.save(myToken);
    }

    public boolean checkIfValidVerificationToken(VerificationToken verificationToken){

        if(verificationToken.getToken() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token is not valid");
        }

        LocalDateTime localeDateTime = LocalDateTime.now();

        if((verificationToken.getExpiryDate().isBefore(localeDateTime))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token has expired");
        }

        return true;
    }
}
