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

    /*
    @Test
    public void loginUser_validInputs_success() {

        // given
        assertNull(userRepository.findByEmail("testEmail"));
        User testUser = new User();
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");
        testUser.setGender(Gender.FEMALE);
        testUser.setBio("asdf");

        User createdUser = userService.createUser(testUser);
        userService.logOutUser(createdUser.getId());

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test.user@uzh.ch");
        userPostDTO.setPassword("testPassword");

        // when
        userService.loginUser(userPostDTO);

        // then
        assertNotNull(createdUser.getToken());
    }
     */

}
