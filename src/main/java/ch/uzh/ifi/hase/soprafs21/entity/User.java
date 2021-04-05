package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Must not be empty")
    @Email(message = "Must be a valid email")
    @Pattern(regexp = "(^$|.+@(.+\\.)?(uzh\\.ch|ethz\\.ch))", message = "You must sing up with an email address belonging to ETH Zurich or UZH")
    private String email;

    @Column
    @NotBlank(message = "Must not be empty")
    private String password;

    @Column
    @NotBlank(message = "Must not be empty")
    private String bio;

    @Column
    @Pattern(regexp = "(^$|(0|\\+41)[0-9]{9})", message = "Must be a valid swiss phone number")
    private String phone;

    @Column(nullable = false, unique = true)
    private String token;

    @Column
    @NotBlank(message = "Must not be empty")
    private String name;

    @Column
    @NotBlank(message = "You must specify your gender identity")
    private Gender gender;

    @Column
    private String profilePicture;

    @Column(nullable = false)
    private LocalDateTime lastSeen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime time) {
        this.lastSeen = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
