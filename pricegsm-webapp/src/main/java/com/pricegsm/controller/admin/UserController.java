package com.pricegsm.controller.admin;

import com.pricegsm.domain.Region;
import com.pricegsm.domain.User;
import com.pricegsm.service.RegionService;
import com.pricegsm.service.UserService;
import com.pricegsm.support.web.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private RegionService regionService;

    @ModelAttribute("regions")
    public List<Region> regions() {
        return regionService.findActive();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @RequestMapping(value = "/user/0", method = RequestMethod.GET)
    public String newUser(Model model) {
        model.addAttribute("user", userService.getDefaultInstance());
        return "admin/user";
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public String editUser(@PathVariable long userId, Model model) {
        model.addAttribute("user", userService.load(userId));
        return "admin/user";
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public String saveUser(@PathVariable long userId, @Valid User user, BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "admin/user";
        }

        userService.save(user);
        MessageHelper.addSuccessAttribute(ra, "alert.saved");
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/user/{userId}/delete", method = RequestMethod.DELETE)
    public String saveUser(@PathVariable long userId, Model model, RedirectAttributes ra) {

        try {
            switch (userService.delete(userId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return "redirect:/admin/user";
    }

}
