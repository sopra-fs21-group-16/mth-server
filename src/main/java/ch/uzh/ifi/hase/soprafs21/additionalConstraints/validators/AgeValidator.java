package ch.uzh.ifi.hase.soprafs21.additionalConstraints.validators;

import ch.uzh.ifi.hase.soprafs21.additionalConstraints.annotationInterfaces.Age;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    protected long minAge;
    protected long maxAge;

    @Override
    public void initialize(Age ageValue) {
        this.minAge = ageValue.minValue();
        this.maxAge = ageValue.maxValue();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        // null values are valid
        if ( date == null ) {
            return true;
        }
        LocalDate today = LocalDate.now();
        // if the age is between minAge and maxAge -> then the age is valid
        return ChronoUnit.YEARS.between(date, today) >= minAge && ChronoUnit.YEARS.between(date, today) <= maxAge;
    }

}
