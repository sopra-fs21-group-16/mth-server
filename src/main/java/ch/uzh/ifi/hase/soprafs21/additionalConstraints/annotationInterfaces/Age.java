package ch.uzh.ifi.hase.soprafs21.additionalConstraints.annotationInterfaces;

import ch.uzh.ifi.hase.soprafs21.additionalConstraints.validators.AgeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE }) // where our annotations can be used
@Retention(RUNTIME) // specify how the marked annotation is stored, choose RUNTIME so it can be used by the runtime environment
@Documented
@Constraint(validatedBy = {AgeValidator.class}) // marks an annotation as being a Bean Validation constraint
public @interface Age {

    // message that will be showed when input data is not valid
    String message() default "Age must be between {minValue} and {maxValue}";

    // allows to split the annotations into different groups to apply different validations
    // to each group-e.g., @Age(group=MALE)
    Class<?>[] groups() default { };

    // payloads are used to carry metadata information consumed by a validation client
    Class<? extends Payload>[] payload() default { };

    // value that will be used to define whether the input value is valid or not
    // e.g. @Age(value=18)
    long minValue();
    long maxValue();
}
