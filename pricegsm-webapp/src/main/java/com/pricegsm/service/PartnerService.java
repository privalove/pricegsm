package com.pricegsm.service;

import com.pricegsm.controller.PartnerUIModel;
import com.pricegsm.dao.OrderDao;
import com.pricegsm.dao.PartnerDao;
import com.pricegsm.dao.PriceListDao;
import com.pricegsm.domain.Partner;
import com.pricegsm.domain.User;
import com.pricegsm.securiry.PrincipalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PartnerService
        extends GlobalEntityService<Partner> {

    @Autowired
    private PartnerDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private PartnerDao partnerDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PriceListDao priceListDao;

    @Override
    protected PartnerDao getDao() {
        return dao;
    }

    public void addPartnership(long userNewPartnerId) {
        User currentUser = userService.loadCurrentUser();
        User userNewPartner = userService.load(userNewPartnerId);
        Partner currentUserCard = new Partner(currentUser, userNewPartner, false, true);
        Partner partnerCard = new Partner(userNewPartner, currentUser, true, false);

        currentUser.getPartners().add(currentUserCard);
        userNewPartner.getPartners().add(partnerCard);

        userService.save(currentUser);
        userService.save(userNewPartner);

        save(currentUserCard);
        save(partnerCard);
    }

    public List<PartnerUIModel> getPartners() {
        List<PartnerUIModel> result = new ArrayList<>();

        User currentUser = userService.loadCurrentUser();
        Set<Partner> partners = currentUser.getPartners();
        for (Partner partner : partners) {
            result.add(createPartnerUIModel(partner));
        }

        return result;
    }

    public PartnerUIModel getPartnerByUser(User user) {
        User currentUser = userService.loadCurrentUser();
        Partner partnerByUser = partnerDao.getPartnerByUser(currentUser.getId(), user.getId());
        return createPartnerUIModel(partnerByUser);
    }

    private PartnerUIModel createPartnerUIModel(Partner partnerByUser) {
        long partnerUserId = partnerByUser.getPartner().getId();
        long currentUserId = partnerByUser.getUser().getId();
        return new PartnerUIModel(
                partnerByUser,
                orderDao.getUserLastOrder(currentUserId, partnerUserId),
                getLastPriceList(partnerUserId));
    }

    private Date getLastPriceList(long partnerUserId) {
        try {
            return priceListDao.getLastPriceList(partnerUserId);
        } catch (NoResultException e) {
            return null;
        }
    }

    public void confirmPartnership(PartnerUIModel partnerUIModel) {
        Partner currentPartnerCard = partnerDao.load(partnerUIModel.getId());
        Partner partnersCard = partnerDao.getPartnerByUser(
                currentPartnerCard.getPartner().getId(), currentPartnerCard.getUser().getId());

        currentPartnerCard.setConfirmed(true);
        partnersCard.setApproved(true);

        userService.save(currentPartnerCard.getUser());
        userService.save(partnersCard.getUser());
    }

    public void deletePartnership(PartnerUIModel partnerUIModel) {
        Partner currentPartnerCard = partnerDao.load(partnerUIModel.getId());
        Partner partnersCard = partnerDao.getPartnerByUser(
                currentPartnerCard.getPartner().getId(), currentPartnerCard.getUser().getId());

        User currentUser = userService.loadCurrentUser();
        User userNewPartner = userService.load(partnerUIModel.getPartnerId());

        currentUser.getPartners().remove(currentPartnerCard);
        userNewPartner.getPartners().remove(partnersCard);

        userService.save(currentUser);
        userService.save(userNewPartner);

        delete(partnerUIModel.getId());
        delete(partnersCard.getId());
    }

    public Partner getPartner(long id) {
        return partnerDao.load(id);
    }
}
