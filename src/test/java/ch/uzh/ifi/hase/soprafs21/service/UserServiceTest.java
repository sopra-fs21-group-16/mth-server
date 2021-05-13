package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.emailAuthentication.VerificationToken;
import ch.uzh.ifi.hase.soprafs21.entities.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

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
        testUser.setPassword("$2a$10$L3NRI.iV3cGcGLvEu2sqle3bi5l2l3L01N/rhNtXjaJV.wMzCDqrS");
        testUser.setEmailVerified(true);

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
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minus(20,ChronoUnit.MINUTES));
        testUser.setToken(UUID.randomUUID().toString());

        // reset LocalDateTime and token
        User user = new User();
        user.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        user.setToken(null);

        // when -> calling findById gives user
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        // log out the user --> test logOutUser
        userService.logOutUser(testUser.getId());

        assertEquals(user.getLastSeen(), testUser.getLastSeen());
        assertEquals(user.getToken(), testUser.getToken());
    }

    @Test
    public void loginUser_validInputs_success(){
        // when
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        User userToLogin = new User();
        userToLogin.setEmail("test.user@uzh.ch");
        userToLogin.setPassword("testPassword");

        // then
        User result = userService.loginUser(userToLogin);

        assertEquals(testUser, result);
    }

    @Test
    public void loginUser_invalidInputs_throwsException(){

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setEmail("test.user@uzh.ch");
        testUser2.setName("Tester2");
        testUser2.setPassword("testWrongPassword");


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);
        userService.logOutUser(testUser.getId());

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);

        // then
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser2));
    }

    @Test
    public void applyUserProfileChange_success(){
        // create user that has to be changed
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minus(20,ChronoUnit.MINUTES));
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setGender(Gender.MALE);

        // create user that provides the new data
        User newUser = new User();
        newUser.setName("NewName");
        newUser.setGender(Gender.FEMALE);

        // change the testUser given the newUser
        userService.applyUserProfileChange(newUser,testUser);

        assertEquals(newUser.getName(), testUser.getName());
        assertEquals(newUser.getGender(), testUser.getGender());
    }

    @Test
    public void applyUserProfileChange_noNewData_throwsException(){
        // create user that has to be changed
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setLastSeen(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minus(20,ChronoUnit.MINUTES));
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setGender(Gender.MALE);

        // create user that provides no new data
        User newUser = new User();
        newUser.setName(null);
        newUser.setGender(null);

        // then -> attempt to change user with no new data -> check that an exception is thrown
        assertThrows(ResponseStatusException.class, () -> userService.applyUserProfileChange(newUser,testUser));
    }

    @Test
    public void checkIfUserExistsWithGivenId_success(){
        // create user that is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");

        // when
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Mockito.any());
        userService.checkIfUserExistsWithGivenId(testUser.getId());
    }

    @Test
    public void checkIfValidToken_success(){
        // create user that has is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User userInRepo = userService.createUser(testUser);

        List<User> userList = new ArrayList<>();
        userList.add(userInRepo);

        given(userService.getUsers()).willReturn(userList);

        boolean valid = userService.checkIfValidToken(userInRepo.getToken());

        assertTrue(valid);
    }

    @Test
    public void checkIfValidToken_invalid_Null(){
        // create user that has is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken(null); // invalid token

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidToken(testUser.getToken()));
    }

    @Test
    public void checkIfValidToken_invalid_ExternalAccess() {
        // create user that has is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken("asdf"); // invalid token that is not in repo

        doThrow(RuntimeException.class).when(userRepository).findByToken(Mockito.any());

        // then
        assertThrows(ResponseStatusException.class, () -> userService.checkIfValidToken(testUser.getToken()));
    }

    @Test
    public void checkIfComputeAge_success(){
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");

        LocalDate now = LocalDate.now().minus(18,ChronoUnit.YEARS);
        testUser.setDateOfBirth(now);

        assertEquals(18,userService.computeAge(now));
    }

    @Test
    public void checkIfGetIdByToken_success(){
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        User userInRepo = userService.createUser(testUser);
        userInRepo.setToken("123");

        given(userRepository.findByToken(userInRepo.getToken())).willReturn(userInRepo);

        assertEquals(testUser.getId(),userService.getIdByToken(userInRepo.getToken()));
    }

    @Test
    public void confirmRegistration_success(){
        // create user that is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken("ssdf");

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        LocalDateTime expiryDate = verificationToken.calculateExpiryDate();
        verificationToken.setExpiryDate(expiryDate);

        // when
        userService.confirmRegistration(verificationToken);

        assertTrue(testUser.getEmailVerified());
    }

    @Test
    public void confirmRegistration_verificationToken_invalid(){
        // create user that is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken(null);

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        LocalDateTime expiryDate = verificationToken.calculateExpiryDate();
        verificationToken.setExpiryDate(expiryDate);

        assertThrows(ResponseStatusException.class, () -> userService.confirmRegistration(verificationToken));
    }

    @Test
    public void confirmRegistration_verificationToken_expired(){
        // create user that is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken("sdsd");

        // expired date
        LocalDateTime localeDateTimeExpired = LocalDateTime.now().minus(2,ChronoUnit.DAYS);

        VerificationToken verificationToken = new VerificationToken(testUser.getToken(),testUser);
        verificationToken.setExpiryDate(localeDateTimeExpired);

        assertThrows(ResponseStatusException.class, () -> userService.confirmRegistration(verificationToken));
    }

    @Test
    public void checkIfEmailVerified_success(){
        // create user that is in repo
        testUser.setId(1L);
        testUser.setEmail("test.user2@uzh.ch");
        testUser.setName("Tester2");
        testUser.setPassword("testPassword2");
        testUser.setToken("ssdf");
        testUser.setEmailVerified(false);

        // when
        assertThrows(ResponseStatusException.class, () -> userService.checkIfEmailVerified(testUser));
    }
}