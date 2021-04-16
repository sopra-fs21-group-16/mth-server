package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;

import java.time.LocalDateTime;

public class UserGetDTOProfile {
    private LocalDateTime lastSeen;
    private String name;
    private Gender gender;
    private String bio;
    private String phone;
    private String profilePicture;
    private UserInterests userInterests;

    public LocalDateTime getLastSeen() {return lastSeen;}

    public void setLastSeen(LocalDateTime lastSeen) {this.lastSeen = lastSeen;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Gender getGender() {return gender;}

    public void setGender(Gender gender) {this.gender = gender;}

    public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}

    public UserInterests getUserInterests() {return userInterests;}

    public void setUserInterests(UserInterests userInterests) {this.userInterests = userInterests;}
}