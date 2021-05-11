package ch.uzh.ifi.hase.soprafs21.accountActivation;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    private LocalDateTime expiryDate;

    private LocalDateTime calculateExpiryDate(int expiryTimeInHours) {
        return LocalDateTime.now().plus(expiryTimeInHours, ChronoUnit.HOURS);
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public LocalDateTime getExpiryDate() {return expiryDate;}

    public void setExpiryDate(LocalDateTime expiryDate) {this.expiryDate = expiryDate;}
}
