package ch.uzh.ifi.hase.soprafs21.emailAuthentication;

import ch.uzh.ifi.hase.soprafs21.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class VerificationToken {
    private static final int expirationInHours = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;

    private LocalDateTime expiryDate;

    public VerificationToken(String token, User user){
        this.token = token;
        this.user = user;
    }

    public VerificationToken(){}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public LocalDateTime getExpiryDate() {return expiryDate;}

    public void setExpiryDate(LocalDateTime expiryDate) {this.expiryDate = expiryDate;}

    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plus(expirationInHours, ChronoUnit.HOURS);
    }
}
