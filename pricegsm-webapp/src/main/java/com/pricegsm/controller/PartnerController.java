package com.pricegsm.controller;

import com.pricegsm.domain.BaseUser;
import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.PartnerService;
import com.pricegsm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/partner/{organizationName}/findPartner.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult findPartner(@PathVariable String organizationName) {
        User user = userService.findByOrganizationName(organizationName);
        if(principalHolder.getCurrentUser().equals(user)) {
            user = null;
        }
        return OperationResult.ok().payload("user", user);
    }

}
