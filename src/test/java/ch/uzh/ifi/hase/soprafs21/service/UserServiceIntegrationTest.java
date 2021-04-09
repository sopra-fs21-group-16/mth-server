package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
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
        testUser.setGender(Gender.FEMALE);
        testUser.setBio("asdf");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getGender(),createdUser.getGender());
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
        testUser.setGender(Gender.MALE);
        testUser.setBio("asdf");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same email
        User testUser2 = new User();

        // change the password but forget about the email
        testUser2.setPassword("testPassword2");
        testUser2.setEmail("test.user@uzh.ch");
        testUser2.setGender(Gender.MALE);
        testUser2.setBio("asdf");

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
        testUser.setGender(Gender.MALE);
        testUser.setBio("asdf");
        User createdUserWithID = userService.createUser(testUser);

        // reset LocalDateTime and token
        User user = new User();
        user.setEmail("test2.user@uzh.ch");
        user.setName("Tester2");
        user.setPassword("testPassword");
        user.setGender(Gender.MALE);
        user.setBio("asdf");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setToken(null);

        // log out the user --> test logOutUser
        userService.logOutUser(createdUserWithID.getId());

        User userWithDeletedToken = userService.getUserByID(createdUserWithID.getId());

        assertEquals(user.getLastSeen(), userWithDeletedToken.getLastSeen());
        assertEquals(user.getToken(), userWithDeletedToken.getToken());
    }

}
