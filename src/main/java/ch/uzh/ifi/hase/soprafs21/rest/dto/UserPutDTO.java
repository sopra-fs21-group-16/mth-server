package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.entities.UserInterests;

public class UserPutDTO {

    private String email;

    private String password;

    private String name;

    private String age;

    private String bio;

    private String phone;

    private Gender gender;

    private String profilePicture;

    private UserInterests userInterests;

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getAge() {return age;}

    public void setAge(String age) {this.age = age;}

    public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public Gender getGender() {return gender;}

    public void setGender(Gender gender) {this.gender = gender;}

    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}

    public UserInterests getUserInterests() {return userInterests;}

    public void setUserInterests(UserInterests userInterests) {this.userInterests = userInterests;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
