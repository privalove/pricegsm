package com.pricegsm.controller.admin;

import com.pricegsm.controller.ChangePasswordForm;
import com.pricegsm.domain.Administrator;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.AdministratorService;
import com.pricegsm.support.web.MessageHelper;
import com.pricegsm.validation.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ProfileAdminController {

    @Autowired
    private PrincipalHolder principalHolder;

    @Autowired
    private AdministratorService administratorService;

    @RequestMapping(value = {"/profile"}, method = RequestMethod.GET)
    public String profile(Model model) {

        model.addAttribute("profileForm", new ProfileForm((Administrator) principalHolder.getCurrentUser()));
        model.addAttribute("changePasswordForm", new ChangePasswordForm());

        return "admin/profile";
    }

    @RequestMapping(value = {"/profile"}, method = RequestMethod.POST)
    public String updateProfile(Model model, @Valid ProfileForm profileForm, BindingResult result, RedirectAttributes ra) {

        if (result.hasErrors()) {
            model.addAttribute("changePasswordForm", new ChangePasswordForm());
            return "admin/profile";
        }

        Administrator user = administratorService.updateProfile(profileForm);

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MessageHelper.addSuccessAttribute(ra, "alert.profileUpdated");

        return "redirect:/admin";
    }

    @RequestMapping(value = {"/profile/changePassword"}, method = RequestMethod.POST)
    public String changePassword(Model model, @Valid ChangePasswordForm changePasswordForm, BindingResult result, RedirectAttributes ra) {
        new PasswordValidator().validate(changePasswordForm, result);

        if (result.hasErrors()) {
            MessageHelper.addErrorAttribute(ra, "alert.passwordError");
            return "redirect:/admin/profile";
        }

        Administrator user = administratorService.changePassword(changePasswordForm.getPassword());

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MessageHelper.addSuccessAttribute(ra, "alert.passwordChanged");

        return "redirect:/admin/profile";

    }


}
