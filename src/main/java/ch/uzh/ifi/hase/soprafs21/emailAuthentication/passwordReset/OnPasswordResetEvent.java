package ch.uzh.ifi.hase.soprafs21.emailAuthentication.passwordReset;

import ch.uzh.ifi.hase.soprafs21.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnPasswordResetEvent extends ApplicationEvent {
    private String appURL;
    private Locale locale;
    private User user;

    public OnPasswordResetEvent(User user, Locale locale, String appUrl){
        // use constructor of ApplicationEvent
        super(user);

        this.user = user;
        this.locale = locale;
        this.appURL = appURL;
    }

    public String getAppURL() {return appURL;}

    public void setAppURL(String appURL) {this.appURL = appURL;}

    public Locale getLocale() {return locale;}

    public void setLocale(Locale locale) {this.locale = locale;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}
}