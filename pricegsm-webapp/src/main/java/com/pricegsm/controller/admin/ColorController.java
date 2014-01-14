package com.pricegsm.controller.admin;

import com.pricegsm.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}
