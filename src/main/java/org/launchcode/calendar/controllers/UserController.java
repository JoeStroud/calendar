package org.launchcode.calendar.controllers;


import org.launchcode.calendar.models.User;
import org.launchcode.calendar.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
@RequestMapping(value="")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {

        model.addAttribute("title", "Log In");
        model.addAttribute("Welcome", "Welcome");

        return "user/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, @RequestParam String username, @RequestParam String password) {

        model.addAttribute("title", "Log In");

        for (User user : userDao.findAll()) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                model.addAttribute("Welcome", "Welcome");
                model.addAttribute("user", user.getName());

                return "user/welcome";
            }
        }

        model.addAttribute("errors", "Invalid login credentials.");
        return "user/login";
    }

    @RequestMapping(value = "sign-up", method = RequestMethod.GET)
    public String signUp(Model model, User user) {

        model.addAttribute("title", "Sign Up");

        model.addAttribute("user", user);
        return "user/sign-up";
    }

    @RequestMapping(value = "sign-up", method = RequestMethod.POST)
    public String processSignUp(Model model, @ModelAttribute @Valid User user, Errors errors) {

        if (errors.hasErrors() || !user.getPassword().equals(user.getVerify())) {
            return "user/sign-up";
        }
        for(User existingUser : userDao.findAll()){
            if (user.getName().equals(existingUser.getName())) {
                model.addAttribute("usernameError", "Account with that username already exists.");
                return "user/sign-up";
            }
        }

        for(User existingUser : userDao.findAll()){
            if (user.getEmail().equals(existingUser.getEmail())) {
                model.addAttribute("emailError", "Account with that email already exists.");
                return "user/sign-up";
            }
        }

        if (!user.getPassword().equals(user.getVerify())) {
            model.addAttribute("passwordError", "Passwords do not match.");
            return "user/sign-up";
        }


        userDao.save(user);
        model.addAttribute("user", user.getName());

        return "user/welcome";
    }


}
