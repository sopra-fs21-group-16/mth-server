package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
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
    public void createUser_validInputs_success(){
        // when -> any object is being saved in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), createdUser.getLastSeen());
        assertNotNull(createdUser.getToken());
    }

    @Test
    public void createUser_duplicateEmail_throwsException(){
        // create an user with not unique email
        userService.createUser(testUser);

        // when -> calling findByEmail gives existing user back -> invokes exception throw
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an exception is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void logOutUser_success(){
        // create user that has to be logged out
        User user = new User();
        user.setId(1L);
        user.setEmail("test.user2@uzh.ch");
        user.setName("Tester2");
        user.setPassword("testPassword2");
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minus(20,ChronoUnit.MINUTES));
        user.setToken(UUID.randomUUID().toString());

        // reset LocalDateTime
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setToken(null);

        // when -> any object is being saved in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // log out the testUser --> test logOutUser
        userService.logOutUser(testUser.getId());

        // when -> calling findById gives mock user
        //Mockito.when(userRepository.findById(Mockito.any())).thenReturn(testUser);

        assertEquals(testUser.getLastSeen(), user.getLastSeen());
        assertEquals(testUser.getToken(), user.getToken());


    }


}
