package com.ds.form;


import com.ds.domain.User;
import com.ds.util.MD5Utils;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;


@Getter
@Setter
public class UserForm {

    //public static final String PHONE_REG = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    @NotBlank
    @Length(min = 1, max = 10)
    private String username;

    @Length(min = 6, max = 50)
    private String password;

    private String confirmPassword;

//    @Pattern(regexp = PHONE_REG, message = "正しい携帯番号を入力してください")
//    private String phone;

    @Email
    @NotBlank
    private String email;

    public UserForm() {
    }

    @AssertTrue(message = "パスワードが一致しません")
    public boolean isConfirmPassValid() {
        if (password == null || confirmPassword == null) {
            return true;
        }
        return this.password.equals(this.confirmPassword);
    }

    public User convertToUser() {
        User user = new UserFormConvert().convert(this);
        return user;
    }

    private class UserFormConvert implements FormConvert<UserForm, User> {

        @Override
        public User convert(UserForm userForm) {
            User user = new User();
            userForm.setPassword(MD5Utils.code(password));
            BeanUtils.copyProperties(userForm, user);
            return user;
        }
    }
}
