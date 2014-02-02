package com.pricegsm.controller.admin;

import com.pricegsm.domain.Specification;
import com.pricegsm.service.SpecificationService;
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

/**
 * User: o.logunov
 * Date: 31.01.14
 * Time: 22:12
 */
@Controller
@RequestMapping(value = "/admin")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @RequestMapping(value = "/specification", method = RequestMethod.GET)
    public String specification(Model model) {
        model.addAttribute("specifications", specificationService.findAll());
        return "admin/specifications";
    }

    @RequestMapping(value = "/specification/0", method = RequestMethod.GET)
    public String newSpecification(Model model) {
        model.addAttribute("specification", specificationService.getDefaultInstance());
        return "admin/specification";
    }

    @RequestMapping(value = "/specification/{specificationId}", method = RequestMethod.GET)
    public String editSpecification(@PathVariable long specificationId, Model model) {
        model.addAttribute("specification", specificationService.load(specificationId));
        return "admin/specification";
    }

    @RequestMapping(value = "/specification/{specificationId}", method = RequestMethod.POST)
    public String saveSpecification(@PathVariable long specificationId, @Valid Specification specification, BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "admin/specification";
        }

        specificationService.save(specification);
        MessageHelper.addSuccessAttribute(ra, "alert.saved.she");
        return "redirect:/admin/specification";
    }

    @RequestMapping(value = "/specification/{specificationId}/delete", method = RequestMethod.DELETE)
    public String saveSpecification(@PathVariable long specificationId, Model model, RedirectAttributes ra) {

        try {
            switch (specificationService.delete(specificationId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted.she");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return "redirect:/admin/specification";
    }
}
