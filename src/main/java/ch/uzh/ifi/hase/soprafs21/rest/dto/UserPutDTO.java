package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;

public class UserPutDTO {

    private Long id;

    private String bio;

    private String phone;

    private Gender gender;

    private String profilePicture;

    private UserInterests userInterests;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public Gender getGender() {return gender;}

    public void setGender(Gender gender) {this.gender = gender;}

    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}

    public UserInterests getUserInterests() {return userInterests;}

    public void setUserInterests(UserInterests userInterests) {this.userInterests = userInterests;}
}
