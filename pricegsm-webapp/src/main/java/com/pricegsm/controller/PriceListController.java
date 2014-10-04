package com.pricegsm.controller;

import com.pricegsm.domain.Price;
import com.pricegsm.domain.PriceList;
import com.pricegsm.domain.PriceListPosition;
import com.pricegsm.domain.User;
import com.pricegsm.jackson.Wrappers;
import com.pricegsm.securiry.PrincipalHolder;
import com.pricegsm.service.*;
import com.pricegsm.util.EntityMetadata;
import com.pricegsm.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Прайслист
 */
@Controller
public class PriceListController {

    @Autowired
    private PriceListService priceListService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private DeliveryPlaceService deliveryPlaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private PrincipalHolder principalHolder;

    @RequestMapping(value = "/price_list", method = RequestMethod.GET)
    public String priceList() {
        return "price_list";
    }

    @RequestMapping(value = "/price_list.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult priceListResource() {
        List<PriceList> priceLists = priceListService.findAllForCurrentUser();

        if (Utils.isEmpty(priceLists)) {
            priceLists = Collections.singletonList(priceListService.getDefaultInstance());
        }

        User user = (User) principalHolder.getCurrentUser();

        return OperationResult.ok()
                .payload("priceLists", priceLists)
                .payload("template", priceListService.getDefaultInstance())
                .payload("positionTemplate", new PriceListPosition())
                .payload("priceTemplate", new Price())
                .payload("vendors", Wrappers.wrap(vendorService.findActive()))
                .payload("products", productService.findActive())
                .payload("specifications", specificationService.findActive())
                .payload("currencies", currencyService.findAll())
                .payload("exchanges", exchangeService.findLast())
                .payload("deliveryPlaces", deliveryPlaceService.findActiveByRegion(user.getRegion().getId()))
                .payload("user", new WorkConditionsForm(user))
                .payload("userMetadata", EntityMetadata.from(User.class));

    }

    @RequestMapping(value = "/price_list/work_conditions.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult updateWorkConditions(@Valid @RequestBody WorkConditionsForm workConditions, BindingResult result) {

        if (result.hasErrors()) {
            return OperationResult
                    .validation()
                    .payload("user", new WorkConditionsForm((User)principalHolder.getCurrentUser()));
        }

        userService.updateWorkConditions(workConditions);

        User user = userService.loadCurrentUser();

        //refresh principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return OperationResult.ok()
                .payload("user", new WorkConditionsForm((User)principalHolder.getCurrentUser()));
    }


    @RequestMapping(value = "/price_list/{position}.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PriceList priceList(@PathVariable int position, HttpServletResponse response)
            throws IOException {
        PriceList priceList = priceListService.findByPositionForCurrentUser(position);

        if (priceList == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return priceList;
    }

    @RequestMapping(value = "/price_list/{position}.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResult savePriceList(@PathVariable int position, @Valid @RequestBody PriceList priceList, BindingResult result, HttpServletResponse response)
            throws IOException {

        if (position < 0 || position > 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        if (result.hasErrors()) {
            return OperationResult.validation()
                    .payload("priceList", priceList);
        }

        priceListService.save(priceList);

        return OperationResult.ok()
                .payload("priceList", priceListService.findByPositionForCurrentUser(position));
    }

}
