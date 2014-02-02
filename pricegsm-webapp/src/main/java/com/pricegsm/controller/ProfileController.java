package com.pricegsm.controller;

import com.pricegsm.domain.User;
import com.pricegsm.jackson.GlobalEntityListWrapper;
import com.pricegsm.jackson.Wrappers;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.RegionService;
import com.pricegsm.service.UserService;
import com.pricegsm.support.web.MessageHelper;
import com.pricegsm.util.EntityMetadata;
import com.pricegsm.validation.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private RegionService regionService;

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

        new PasswordValidator().validate(registrationForm, result);

        if (result.hasErrors()) {
            return "signup/signup";
        }
        userService.save(registrationForm.toUser(userService.getDefaultInstance()));

        return "redirect:/";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        return "profile";
    }

    @RequestMapping(value = "/profile.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProfileForm currentUser() {
        return new ProfileForm((User) principalHolder.getCurrentUser());
    }

    @RequestMapping(value = "/profile/context.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult context() {
        return OperationResult.ok()
                .payload("regions", Wrappers.wrap(regionService.findActive()))
                .payload("metadata", EntityMetadata.from(ProfileForm.class));
    }


    @RequestMapping(value = "/profile.json", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult updateProfile(@RequestBody @Valid ProfileForm profilerForm, BindingResult result) {

        if (result.hasErrors()) {
            return OperationResult.validation()
                    .payload("profile", new ProfileForm((User) principalHolder.getCurrentUser()));
        }

        userService.updateProfile(profilerForm);

        User user =  userService.loadCurrentUser();

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return OperationResult.ok()
                .payload("profile", new ProfileForm((User) principalHolder.getCurrentUser()));
    }

    @RequestMapping(value = {"/profile/changePassword"}, method = RequestMethod.POST)
    @ResponseBody
    public OperationResult changePassword( @RequestBody @Valid ChangePasswordForm changePasswordForm, BindingResult result, RedirectAttributes ra) {
        new PasswordValidator().validate(changePasswordForm, result);

        if (result.hasErrors()) {
            return OperationResult
                    .validation()
                    .message("alert.passwordError");
        }

        userService.changePassword(changePasswordForm.getPassword());

        User user = userService.loadCurrentUser();

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return OperationResult.ok().message("alert.passwordChanged");

    }


}
