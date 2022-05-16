package com.ds.util;

import com.ds.domain.UserRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

public class LoginValidator implements ConstraintValidator<Login, Object> {

    @Autowired
    private UserRepository userRepository;

    private String username;
    private String password;

    @Override
    public void initialize(Login annotation) {
        this.username = annotation.fieldUsername();
        this.password = annotation.fieldPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        var username = (String) beanWrapper.getPropertyValue(this.username);
        var password = (String) beanWrapper.getPropertyValue(this.password);

        var user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        return user != null;

    }
}
