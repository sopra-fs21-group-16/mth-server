package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.emailAuthentication.VerificationToken;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("verificationTokenRepository")
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByEmail("testEmail"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getName(), createdUser.getName());
        assertNotNull(createdUser.getToken());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), createdUser.getLastSeen());

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void createUser_duplicateEmail_throwsException() {
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same email
        User testUser2 = new User();

        // change the password but forget about the email
        testUser2.setPassword("testPassword2");
        testUser.setName("Tester2");
        testUser2.setEmail("test.user@uzh.ch");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void createUser_logOutUser_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        // reset LocalDateTime and token
        User user = new User();
        user.setEmail("test2.user@uzh.ch");
        user.setName("Tester2");
        user.setPassword("testPassword2");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setToken(null);

        // log out the user --> test logOutUser
        userService.logOutUser(createdUserWithID.getId());

        User userWithDeletedToken = userService.getUserByID(createdUserWithID.getId());

        assertEquals(user.getLastSeen(), userWithDeletedToken.getLastSeen());
        assertEquals(user.getToken(), userWithDeletedToken.getToken());

        //delete specific users
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void loginUser_validInputs_success() {
        // given
        assertNull(userRepository.findByEmail("testEmail"));
        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        testUser.setEmailVerified(true);

        User createdUser = userService.createUser(testUser);
        userService.logOutUser(createdUser.getId());

        User userToLogin = new User();
        userToLogin.setEmail("test.user@uzh.ch");
        userToLogin.setPassword("testPassword");
        // when
        userService.loginUser(userToLogin);

        // then
        assertNotNull(createdUser.getToken());

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void updateUserProfile_Success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        // user with new data
        User newUser = new User();
        newUser.setEmail("newName@uzh.ch");
        newUser.setName("newName");
        newUser.setDateOfBirth(LocalDate.of(2000, 1, 8));
        newUser.setBio("newBio");
        newUser.setPhone("+41791231212");
        newUser.setGender(Gender.MALE);
        newUser.setProfilePicture("FakeLink");

        // when
        userService.applyUserProfileChange(newUser, testUser);

        // then
        assertEquals(newUser.getName(), testUser.getName());

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void updateUserProfile_InvalidChange_NoSuccess(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        // user with new data
        User newUser = new User();
        newUser.setName(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.applyUserProfileChange(newUser, testUser));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfValidToken_Success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        boolean valid = userService.checkIfValidToken(createdUserWithID.getToken());

        assertTrue(valid);

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfValidToken_Invalid_NoSuccess(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);
        createdUserWithID.setToken(null);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidToken(createdUserWithID.getToken()));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfValidToken_WrongToken_NoSuccess(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidToken("wrong token"));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfComputeAge_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        LocalDate now = LocalDate.now().minus(18,ChronoUnit.YEARS);
        testUser.setDateOfBirth(now);

        assertEquals(18,userService.computeAge(now));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfGetIdByToken_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        assertEquals(createdUserWithID.getId(),userService.getIdByToken(createdUserWithID.getToken()));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void confirmRegistration_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        LocalDateTime expiryDate = verificationToken.calculateExpiryDate();
        verificationToken.setExpiryDate(expiryDate);

        // when
        userService.confirmRegistration(verificationToken);

        assertTrue(createdUserWithID.getEmailVerified());

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void isUserAuthenticated_success(){
        assertNull(userRepository.findByEmail("authenticated.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("authenticated.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        assertTrue(userService.isUserAuthenticated(createdUser.getId(),createdUser.getToken()));

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void isUserAuthenticated_WrongID(){
        assertNull(userRepository.findByEmail("authenticated.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("authenticated.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        assertThrows(ResponseStatusException.class, () -> userService.isUserAuthenticated(100000L,createdUser.getToken()));


        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void isUserAuthenticated_WrongToken(){
        assertNull(userRepository.findByEmail("authenticated.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("authenticated.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        assertThrows(ResponseStatusException.class, () -> userService.isUserAuthenticated(createdUser.getId(),"WrongToken"));


        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void isUserAuthorized_success(){
        assertNull(userRepository.findByEmail("authorized.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("authorized.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        assertTrue(userService.isUserAuthorized(createdUser.getId(), createdUser.getId(), createdUser.getToken()));

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void isUserAuthorized_WrongID(){
        assertNull(userRepository.findByEmail("authorized.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("authorized.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when
        User createdUser = userService.createUser(testUser);

        assertThrows(ResponseStatusException.class, () -> userService.isUserAuthorized(11L, createdUser.getId(), createdUser.getToken()));

        //delete specific user
        userRepository.delete(createdUser);
    }

    @Test
    public void checkIfValidVerificationToken_verificationToken_invalid(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);
        createdUserWithID.setToken(null);

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        LocalDateTime expiryDate = verificationToken.calculateExpiryDate();
        verificationToken.setExpiryDate(expiryDate);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidVerificationToken(verificationToken));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIFValidVerificationToken_verificationToken_expired(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        // expired date
        LocalDateTime localeDateTimeExpired = LocalDateTime.now().minus(2,ChronoUnit.DAYS);

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        verificationToken.setExpiryDate(localeDateTimeExpired);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidVerificationToken(verificationToken));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfEmailVerified_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        // when
        // exception is thrown because initial value of emailVerified is set to false
        assertThrows(ResponseStatusException.class, () -> userService.checkIfEmailVerified(createdUserWithID));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }

    @Test
    public void checkIfEmailExists_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User createdUserWithID = userService.createUser(testUser);

        assertTrue(userService.checkIfEmailExists(createdUserWithID.getEmail()));

        //delete specific user
        userRepository.delete(createdUserWithID);
    }
}
