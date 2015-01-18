package com.pricegsm.controller;

import com.pricegsm.domain.Partner;
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
        return OperationResult.ok().payload("partners", partnerService.getPartners());
    }

    @RequestMapping(value = "/partner/findOrganization.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult findPartner(@RequestBody OrganizationName organizationName, BindingResult result) {
        User user = userService.findByOrganizationName(organizationName.getOrganizationName());
        if (principalHolder.getCurrentUser().equals(user)) {
            user = null;
        }
        return OperationResult.ok().payload("user", user);
    }

    @RequestMapping(value = "partner/sendRequest.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult sendRequest(@RequestBody User user, BindingResult result) {
        partnerService.addPartnership(user.getId());
        return OperationResult.ok().payload("partner", partnerService.getPartnerByUser(user));
    }

    @RequestMapping(value = "partner/addPartner.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult addPartner(@RequestBody PartnerUIModel partner, BindingResult result) {
        partnerService.confirmPartnership(partner);
        return OperationResult.ok().payload("partner", partnerService.getPartner(partner.getId()));
    }

    @RequestMapping(value = "partner/deletePartner.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult deletePartner(@RequestBody PartnerUIModel partner, BindingResult result) {
        partnerService.deletePartnership(partner);
        return OperationResult.ok();
    }

}
