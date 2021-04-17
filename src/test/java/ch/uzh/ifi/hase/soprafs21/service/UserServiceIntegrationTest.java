package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
        testUser.setEmail("test.user@gmail.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidEmail(testUser.getEmail()));
    }
}
