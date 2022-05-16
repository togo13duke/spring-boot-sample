package com.ds.service;


import com.ds.domain.User;
import com.ds.domain.UserRepository;
import com.ds.form.UserForm;
import com.ds.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password){
        var user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public void registerUser(UserForm userForm){
        var user = userForm.convertToUser();
        userRepository.save(user);
    }
}
