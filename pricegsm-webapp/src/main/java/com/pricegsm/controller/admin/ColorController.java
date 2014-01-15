package com.pricegsm.controller.admin;

import com.pricegsm.domain.Color;
import com.pricegsm.service.ColorService;
import com.pricegsm.support.web.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @RequestMapping(value = "/color", method = RequestMethod.GET)
    public String colors(Model model) {
        model.addAttribute("colors", colorService.findAll());
        return "admin/colors";
    }

    @RequestMapping(value = "/color/0", method = RequestMethod.GET)
    public String newColor(Model model) {
        model.addAttribute("color", colorService.getDefaultInstance());
        return "admin/color";
    }

    @RequestMapping(value = "/color/{colorId}", method = RequestMethod.GET)
    public String editColor(@PathVariable long colorId, Model model) {
        model.addAttribute("color", colorService.load(colorId));
        return "admin/color";
    }

    @RequestMapping(value = "/color/{colorId}", method = RequestMethod.POST)
    public String saveColor(@PathVariable long colorId, @Valid Color color, BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "admin/color";
        }

        colorService.save(color);
        MessageHelper.addSuccessAttribute(ra, "alert.saved");
        return "redirect:/admin/color";
    }

    @RequestMapping(value = "/color/{colorId}/delete", method = RequestMethod.DELETE)
    public String saveColor(@PathVariable long colorId, Model model, RedirectAttributes ra) {

        try {
            switch (colorService.delete(colorId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return "redirect:/admin/color";
    }

}
