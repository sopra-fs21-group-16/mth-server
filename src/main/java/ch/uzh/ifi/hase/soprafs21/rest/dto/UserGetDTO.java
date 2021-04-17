package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;

import java.time.LocalDateTime;

public class UserGetDTO {

    private Long id;
    private String email;
    private LocalDateTime lastSeen;
    private String name;
    private int age;
    private Gender gender;
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

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

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
