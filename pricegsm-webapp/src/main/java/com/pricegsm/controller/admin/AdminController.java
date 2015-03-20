package com.pricegsm.controller.admin;

import com.pricegsm.service.PriceListParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private PriceListParserService priceListParserService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String index() {
        return "admin/index";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file, @RequestParam("seller") String seller) {
        priceListParserService.parse(file, seller);

        return "redirect:/";
    }
}
