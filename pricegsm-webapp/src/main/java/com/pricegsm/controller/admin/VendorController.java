package com.pricegsm.controller.admin;

import com.pricegsm.domain.Vendor;
import com.pricegsm.service.VendorService;
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
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @RequestMapping(value = "/vendor", method = RequestMethod.GET)
    public String vendors(Model model) {
        model.addAttribute("vendors", vendorService.findAll());
        return "admin/vendors";
    }

    @RequestMapping(value = "/vendor/0", method = RequestMethod.GET)
    public String newVendor(Model model) {
        model.addAttribute("vendor", vendorService.getDefaultInstance());
        return "admin/vendor";
    }

    @RequestMapping(value = "/vendor/{vendorId}", method = RequestMethod.GET)
    public String editVendor(@PathVariable long vendorId, Model model) {
        model.addAttribute("vendor", vendorService.load(vendorId));
        return "admin/vendor";
    }

    @RequestMapping(value = "/vendor/{vendorId}", method = RequestMethod.POST)
    public String saveVendor(@PathVariable long vendorId, @Valid Vendor vendor, BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "admin/vendor";
        }

        vendorService.save(vendor);
        MessageHelper.addSuccessAttribute(ra, "alert.saved");
        return "redirect:/admin/vendor";
    }

    @RequestMapping(value = "/vendor/{vendorId}/delete", method = RequestMethod.DELETE)
    public String saveVendor(@PathVariable long vendorId, RedirectAttributes ra) {

        try {
            switch (vendorService.delete(vendorId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return "redirect:/admin/vendor";
    }

}
