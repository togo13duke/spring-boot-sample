package com.ds.form;

import com.ds.util.Login;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Login
public class LoginForm {
    private String username;
    private String password;
}
