package ch.uzh.ifi.hase.soprafs21.entities;

import ch.uzh.ifi.hase.soprafs21.constant.ActivityCategory;
import ch.uzh.ifi.hase.soprafs21.constant.AgeRange;
import ch.uzh.ifi.hase.soprafs21.constant.Gender;
import ch.uzh.ifi.hase.soprafs21.constant.GenderPreference;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "USER_INTERESTS")
public class UserInterests implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int ageRangeMin;

    @Column
    private int ageRangeMax;

    @Enumerated(EnumType.STRING)
    private GenderPreference genderPreference;

    @ElementCollection(targetClass = ActivityCategory.class)
    @CollectionTable(name = "USER_INTERESTS_ACITIVITY", joinColumns = @JoinColumn(name = "userInterestsId"))
    @Column
    @Enumerated(EnumType.STRING)
    private Set<ActivityCategory> activityInterests;

    @OneToOne
    private User user;

    public AgeRange getAgeRange() {
        return new AgeRange(ageRangeMin, ageRangeMax);
    }

    public void setAgeRange(AgeRange ageRange) {
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

    public void setActivityInterests(Set<ActivityCategory> activityInterests) {
        this.activityInterests = activityInterests;
    }
}
