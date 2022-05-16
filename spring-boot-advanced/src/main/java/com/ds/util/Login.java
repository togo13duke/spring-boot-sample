package com.ds.util;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {LoginValidator.class})
public @interface Login {

    String message() default "ユーザ名、もしくはパスワードが間違っています";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

    String fieldUsername() default "username";
    String fieldPassword() default "password";
}
