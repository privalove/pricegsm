package com.pricegsm.controller.admin;

import com.pricegsm.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @RequestMapping("/color")
    public String colors(Model model) {
        model.addAttribute("colors", colorService.findAll());
        return "admin/color";
    }
}
