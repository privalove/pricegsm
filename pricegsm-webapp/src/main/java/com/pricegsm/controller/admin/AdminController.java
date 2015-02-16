package com.pricegsm.controller.admin;

import com.pricegsm.service.ColorService;
import com.pricegsm.service.PriceListParserService;
import com.pricegsm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        priceListParserService.parse(file);

        return "redirect:/";
    }
}
