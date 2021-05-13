package ch.uzh.ifi.hase.soprafs21.rest.dto.userDTO;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserGetDTO {

    private Long id;
    private String email;
    private LocalDateTime lastSeen;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private boolean emailVerified;
    private String bio;
    private String token;
    private String phone;
    private String profilePicture;
    private UserInterests userInterests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime time) {
        this.lastSeen = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {return dateOfBirth;}

    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean getEmailVerified() {return emailVerified;}

    public void setEmailVerified(boolean emailVerified) {this.emailVerified = emailVerified;}

    public String getBio() {return bio;}

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}

    public UserInterests getUserInterests() {return userInterests;}

    public void setUserInterests(UserInterests userInterests) {this.userInterests = userInterests;}
}
