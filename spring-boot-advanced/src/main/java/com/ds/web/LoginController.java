package com.ds.web;


import com.ds.domain.User;
import com.ds.domain.UserRepository;
import com.ds.form.LoginForm;
import com.ds.form.UserForm;
import com.ds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form) {
        return "login";

    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult result,
                        HttpSession session){

        if(result.hasErrors()){
            return "login";
        }

        session.setAttribute("user",form.getUsername());
        return "index";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userForm",new UserForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserForm userForm, BindingResult result) {

        if (result.hasErrors()){
            return "register";
        }
        userService.registerUser(userForm);
        return "redirect:/login";
    }

    @GetMapping("/exception")
    public String testException(){
        throw new RuntimeException();
    }


}
