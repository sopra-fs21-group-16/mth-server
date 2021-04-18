package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

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
        assertEquals(testUser.getName(),createdUser.getName());
        assertNotNull(createdUser.getToken());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), createdUser.getLastSeen());
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
    }

    @Test
    public void loginUser_validInputs_success() {
        // given
        assertNull(userRepository.findByEmail("testEmail"));
        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);
        userService.logOutUser(createdUser.getId());

        // when
        userService.loginUser(testUser);

        // then
        assertNotNull(createdUser.getToken());
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
        newUser.setName("newName");

        // when
        userService.applyUserProfileChange(newUser,testUser);

        // then
        assertEquals(newUser.getName(),testUser.getName());
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
        assertThrows(ResponseStatusException.class, () -> userService.applyUserProfileChange(newUser,testUser));
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

        assertEquals(true,valid);
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
    }

    @Test
    public void checkIfValidEmail_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        User createdUserWithID = userService.createUser(testUser);

        boolean valid = userService.checkIfValidEmail(createdUserWithID.getEmail());

        assertTrue(valid);
    }

    @Test
    public void checkIfValidEmail_invalid(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@gmail.ch"); // invalid email
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidEmail(testUser.getEmail()));
    }

    @Test
    public void checkIfValidEmail_invalid_Null(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail(null);
        testUser.setName("Tester");
        testUser.setPassword("sasfadf");  // invalid password
        testUser.setPhone("0791235666");

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidEmail(testUser.getEmail()));
    }

    @Test
    public void checkIfValidPhone_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        testUser.setPhone("0791233455");
        User createdUserWithID = userService.createUser(testUser);

        boolean valid = userService.checkIfValidPhone(createdUserWithID.getPhone());

        assertTrue(valid);
    }

    @Test
    public void checkIfValidPhone_invalid(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@gmail.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        testUser.setPhone("333"); // invalid phone number

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidPhone(testUser.getPhone()));
    }

    @Test
    public void checkIfValidPassword_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        testUser.setPhone("0791233455");
        User createdUserWithID = userService.createUser(testUser);

        boolean valid = userService.checkIfValidPassword(createdUserWithID.getPassword());

        assertTrue(valid);
    }

    @Test
    public void checkIfValidPassword_invalid(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@gmail.ch");
        testUser.setName("Tester");
        testUser.setPassword(null);  // invalid password
        testUser.setPhone("0791235666");

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidPassword(testUser.getPassword()));
    }

    @Test
    public void checkIfValidName_success(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("tester");
        testUser.setPassword("testPassword");
        testUser.setPhone("0791233455");
        User createdUserWithID = userService.createUser(testUser);

        boolean valid = userService.checkIfValidName(createdUserWithID.getName());

        assertTrue(valid);
    }

    @Test
    public void checkIfValidName_invalid(){
        assertNull(userRepository.findByEmail("test.user@uzh.ch"));

        User testUser = new User();
        testUser.setEmail("test.user@gmail.ch");
        testUser.setName(null); // invalid name
        testUser.setPassword("sdsf");
        testUser.setPhone("0791235666");

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidName(testUser.getName()));
    }

}
