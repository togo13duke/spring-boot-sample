package com.ds.service;

import com.ds.domain.User;
import com.ds.form.UserForm;

public interface UserService {

    User checkUser(String username, String password);

    void registerUser(UserForm userForm);

}
