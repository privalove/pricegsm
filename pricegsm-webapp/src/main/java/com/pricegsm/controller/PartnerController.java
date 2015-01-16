package com.pricegsm.controller;

import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.PartnerService;
import com.pricegsm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контрагенты
 */
@Controller
public class PartnerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PrincipalHolder principalHolder;

    @RequestMapping(value = "/partner", method = RequestMethod.GET)
    public void partner() {
    }

    @RequestMapping(value = "/partner/partners.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult partners() {
        return OperationResult.ok().payload("partners", partnerService.findAll());
    }

    @RequestMapping(value = "/partner/findOrganization.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult findPartner(@RequestBody OrganizationName organizationName, BindingResult result) {
        User user = userService.findByOrganizationName(organizationName.getOrganizationName());
        if(principalHolder.getCurrentUser().equals(user)) {
            user = null;
        }
        return OperationResult.ok().payload("user", user);
    }

}
