package com.pricegsm.controller;

import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Профиль
 */
@Controller
public class ProfileController {

    @Autowired
    private PrincipalHolder principalHolder;

    @Autowired
    private UserService userService;

    @RequestMapping({"/signin"})
    public String login() {
        if (principalHolder.isAuthorized()) {
            return "redirect:/";
        } else {
            return "signin/signin";
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "signup/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String register(@Valid RegistrationForm registrationForm, BindingResult result) {

        if (result.hasErrors()) {
            return "signup/signup";
        }
        //user.setId(0);
        //userService.save(user);

        return "redirect:/";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        return "profile";
    }

    @RequestMapping(value = "/profile.json", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult currentUser() {
        return OperationResult.ok().payload("user", principalHolder.getCurrentUser());
    }

    @RequestMapping(value = "/profile.json", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult updateProfile(@RequestBody @Valid User user, BindingResult result) {

        if (result.hasErrors()) {
            return OperationResult.validation()
                    .payload("user", principalHolder.getCurrentUser());
        }
        user.setId(principalHolder.getCurrentUser().getId());
        userService.save(user);

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return OperationResult.ok()
                .payload("user", principalHolder.getCurrentUser());
    }

}
