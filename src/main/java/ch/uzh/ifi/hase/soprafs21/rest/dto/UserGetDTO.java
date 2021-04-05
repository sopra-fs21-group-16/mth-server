package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

public class UserGetDTO {

    private Long id;
    private String email;
    private LocalDateTime lastSeen;

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
}
