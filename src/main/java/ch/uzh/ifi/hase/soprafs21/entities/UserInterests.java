package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.AgeRange;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "UserInterests")
public class UserInterests implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Min(18)
    private int ageRangeMin;

    @Column
    @Max(120)
    private int ageRangeMax;

    @Enumerated(EnumType.STRING)
    private GenderPreference genderPreference;

    @ElementCollection(targetClass = ActivityCategory.class)
    @CollectionTable(name = "USER_INTERESTS_ACTIVITY", joinColumns = @JoinColumn(name = "userInterestsId"))
    @Column
    @Enumerated(EnumType.STRING)
    private Set<ActivityCategory> activityInterests;

    @OneToOne
    private User user;

    public AgeRange getAgeRange() {
        return new AgeRange(ageRangeMin, ageRangeMax);
    }

    public void setAgeRange(AgeRange ageRange) {
        if(ageRange.min > ageRange.max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The age range is invalid");
        }

        ageRangeMin = ageRange.min;
        ageRangeMax = ageRange.max;
    }

    public GenderPreference getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(GenderPreference genderPreference) {
        this.genderPreference = genderPreference;
    }

    public Set<ActivityCategory> getActivityInterests() {
        return activityInterests;
    }

    public void setActivityInterests(Set<ActivityCategory> activityInterests) {this.activityInterests = activityInterests;}

    public void updateUserInterests(UserInterests userInterests){
        if(userInterests.getAgeRange().min > userInterests.getAgeRange().max){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The age range is invalid");
        }

        this.genderPreference = userInterests.genderPreference;
        this.activityInterests = userInterests.activityInterests;
        this.ageRangeMin = userInterests.getAgeRange().min;
        this.ageRangeMax = userInterests.getAgeRange().max;
    }
}
