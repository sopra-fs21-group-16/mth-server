package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test.user@uzh.ch");
        testUser.setName("Tester");
        testUser.setPassword("testPassword");

        // when -> any object is being saved in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void loginUser_validInputs_success(){

        User createdUser = userService.createUser(testUser);
        userService.logOutUser(testUser.getId());
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test.user@uzh.ch");
        userPostDTO.setPassword("testPassword");

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(createdUser);

        // then
        userService.loginUser(userPostDTO);

        assertNotNull(createdUser.getToken());
    }

    @Test
    public void loginUser_invalidInputs_throwsException(){

        User createdUser = userService.createUser(testUser);
        userService.logOutUser(testUser.getId());
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("test.user@uzh.ch");
        userPostDTO.setPassword("testPasword");

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(createdUser);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(userPostDTO));
    }
}
