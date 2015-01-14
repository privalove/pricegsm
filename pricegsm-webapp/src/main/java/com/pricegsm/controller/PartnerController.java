package com.pricegsm.controller;

import com.pricegsm.service.PartnerService;
import com.pricegsm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Контрагенты
 */
@Controller
public class PartnerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PartnerService partnerService;

    @RequestMapping(value = "/partner", method = RequestMethod.GET)
    public void partner() {
    }

    @RequestMapping(value = "/partner/partners.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult partners() {
        return OperationResult.ok().payload("partners", partnerService.findAll());
    }

}
