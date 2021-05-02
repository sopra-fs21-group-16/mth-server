package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "User")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Must be a valid email")
    @Pattern(regexp = "(^$|.{1,255}@(.{1,255}\\.)?(uzh\\.ch|ethz\\.ch))", message = "You must sing up with an email address belonging to ETH Zurich or UZH")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be empty")
    private String password;

    @Column
    private String bio;

    @Column
    @Pattern(regexp = "(^$|(0|\\+41)[0-9]{9})", message = "Must be a valid swiss phone number")
    private String phone;

    @Column(unique = true)
    private String token;

    @Column
    @NotBlank(message = "Name must not be empty")
    private String name;

    /** TODO: Maybe set constraints for the date of Birth */
    @Column
    @Past
    private LocalDate dateOfBirth;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String profilePicture;

    @Column(nullable = false)
    private LocalDateTime lastSeen;

    // cascade option enabled to map objects
    @OneToOne(cascade = {CascadeType.ALL})
    private UserInterests userInterests;

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

    public String getToken(){ return token;}

    public void setToken(String token){ this.token = token;}

    public String getName(){ return name;}

    public void setName(String name){ this.name = name;}

    public LocalDate getDateOfBirth() {return dateOfBirth;}

    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public Gender getGender() {return gender; }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public UserInterests getUserInterests() {return userInterests;}

    public void setUserInterests(UserInterests userInterests) {this.userInterests = userInterests;}

    public int getAge(){
        // if no date of birth is set yet, we throw exception
        if (this.dateOfBirth == null ){
            return 0;
        }

        // convert extracted data and get local data
        LocalDate localDateOfDateOfBirth = dateOfBirth;
        LocalDate now = LocalDate.now();
        Period differenceOfDates = Period.between(localDateOfDateOfBirth,now);

        // compute age
        return differenceOfDates.getYears();
    }
}
