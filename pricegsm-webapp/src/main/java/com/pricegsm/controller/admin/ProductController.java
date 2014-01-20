package com.pricegsm.controller.admin;

import com.pricegsm.domain.Color;
import com.pricegsm.domain.Product;
import com.pricegsm.domain.ProductType;
import com.pricegsm.domain.Vendor;
import com.pricegsm.service.ColorService;
import com.pricegsm.service.ProductService;
import com.pricegsm.service.ProductTypeService;
import com.pricegsm.service.VendorService;
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
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private VendorService vendorService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private ProductTypeService productTypeService;

    @ModelAttribute("colors")
    public List<Color> colors() {
        return colorService.findActive();
    }

    @ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorService.findActive();
    }

    @ModelAttribute("type")
    public List<ProductType> types() {
        return productTypeService.findActive();
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @RequestMapping(value = "/product/0", method = RequestMethod.GET)
    public String newProduct(Model model) {
        model.addAttribute("product", productService.getDefaultInstance());
        
        return "admin/product";
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public String editProduct(@PathVariable long productId, Model model) {
        model.addAttribute("product", productService.load(productId));
        return "admin/product";
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public String saveProduct(@PathVariable long productId, @Valid Product product, BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "admin/product";
        }

        productService.save(product);
        MessageHelper.addSuccessAttribute(ra, "alert.saved");
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/product/{productId}/delete", method = RequestMethod.DELETE)
    public String saveProduct(@PathVariable long productId, Model model, RedirectAttributes ra) {

        try {
            switch (productService.delete(productId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return "redirect:/admin/product";
    }

}
