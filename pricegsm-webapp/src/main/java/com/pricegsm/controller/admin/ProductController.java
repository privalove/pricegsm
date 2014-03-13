package com.pricegsm.controller.admin;

import com.pricegsm.domain.*;
import com.pricegsm.service.*;
import com.pricegsm.support.web.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
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

    @ModelAttribute("types")
    public List<ProductType> types() {
        return productTypeService.findActive();
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String products(Model model) {
        model.addAttribute("products", productService.getProductsGroupingByYandexId());
        model.addAttribute("vendorId", 0);
        return "admin/products";
    }

    @RequestMapping(value = "/product/0/{vendorId}", method = RequestMethod.GET)
    public String newProduct(@PathVariable long vendorId, Model model) {
        ProductForm productForm = new ProductForm(Arrays.asList(productService.getDefaultInstance()));
        productForm.setYandexId("0");
        model.addAttribute("productForm", productForm);
        model.addAttribute("vendorId", vendorId);
        return "admin/product";
    }

    @RequestMapping(value = "/product/{yandexId}/{vendorId}", method = RequestMethod.GET)
    public String editProduct(@PathVariable String yandexId, @PathVariable long vendorId, Model model) {
        model.addAttribute("productForm", productService.getProductForm(yandexId));
        model.addAttribute("vendorId", vendorId);
        return "admin/product";
    }

    @RequestMapping(value = "/product/{yandexId}/{vendorId}", params = {"addColor"}, method = RequestMethod.POST)
    public String addColor(
            @PathVariable long vendorId,
            final ProductForm productForm, Model model) {

        productForm.getColors().add(new ColorProductForm(0L, colorService.load(1L), ""));
        model.addAttribute("vendorId", vendorId);
        return "admin/product";
    }

    @RequestMapping(value = "/product/{yandexId}/{vendorId}", params = {"removeColor"}, method = RequestMethod.POST)
    public String removeColor(
            @RequestParam final int removeColor,
            @PathVariable long vendorId,
            final ProductForm productForm, Model model) {
        final Long productId = productForm.getColors().get(removeColor).getProductId();

        if (productId == 0L) {
            productForm.getColors().remove(removeColor);
        } else {
            try {
                switch (productService.delete(productId)) {
                    case OK:
                        MessageHelper.addSuccessAttribute(model, "alert.deleted");
                        productForm.getColors().remove(removeColor);
                        break;
                }
            } catch (Exception e) {
                MessageHelper.addWarningAttribute(model, "alert.isUsed");
            }
        }

        model.addAttribute("vendorId", vendorId);
        return "admin/product";
    }

    @RequestMapping(value = "/product/{yandexId}/{vendorId}", method = RequestMethod.POST)
    public String saveProduct(
            @PathVariable long vendorId, @Valid ProductForm productForm,
            BindingResult result, Model model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            model.addAttribute("vendorId", vendorId);
            return "admin/product";
        }

        productService.saveProductFromProductForm(productForm);
        MessageHelper.addSuccessAttribute(ra, "alert.saved");
        return getVendorUrl(vendorId);
    }

    @RequestMapping(value = "/product/{yandexId}/{vendorId}/delete", method = RequestMethod.DELETE)
    public String saveProduct(
            @PathVariable final String yandexId, @PathVariable long vendorId, RedirectAttributes ra) {
        try {
            switch (productService.deleteProductsByYandexId(yandexId)) {
                case OK:
                    MessageHelper.addSuccessAttribute(ra, "alert.deleted");
                    break;
            }
        } catch (Exception e) {
            MessageHelper.addWarningAttribute(ra, "alert.isUsed");
        }

        return getVendorUrl(vendorId);
    }

    private String getVendorUrl(long vendorId) {
        return vendorId == 0 ? "redirect:/admin/product" : "redirect:/admin/product/" + vendorId + "/vendor";
    }

    @RequestMapping(value = "/product/{vendorId}/vendor", method = RequestMethod.GET)
    public String productsByVendor(@PathVariable long vendorId, Model model) {
        model.addAttribute("products", productService.findByVendor(vendorId));
        model.addAttribute("vendorId", vendorId);
        return "admin/products";
    }
}
