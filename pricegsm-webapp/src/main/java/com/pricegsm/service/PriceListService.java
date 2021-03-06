package com.pricegsm.service;

import com.pricegsm.dao.*;
import com.pricegsm.domain.*;
import com.pricegsm.util.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class PriceListService
        extends BaseEntityService<PriceList, PriceListPK> {

    @Autowired
    private PriceListDao dao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private PartnerDao partnerDao;

    @Override
    protected PriceListDao getDao() {
        return dao;
    }

    @Override
    protected PriceList preSave(PriceList entity) {
        PriceList priceList = super.preSave(entity);

        priceList.setUser(getCurrentUser());

        if (priceList.getPosition() == 0) {
            Date today = Utils.today();
            Date startDate =
                    today.getTime() <= entity.getUser().getDeadLine().getTime() ?
                    today : DateUtils.addDays(today, 1);
            priceList.setSellFromDate(startDate);
            priceList.setSellToDate(DateUtils.addDays(startDate, 1));
        }

        for (PriceListPosition position : entity.getPositions()) {
            position.setPriceList(priceList);

            for (Price price : position.getPrices()) {
                price.setPriceListPosition(position);
            }
        }

        return priceList;
    }

    @Override
    public PriceList getDefaultInstance() {
        PriceList result = super.getDefaultInstance();

        result.setUser(getCurrentUser());
        result.setSellFromDate(Utils.today());
        result.setSellToDate(DateUtils.addDays(Utils.today(), 1));
        result.setCurrency(currencyDao.load(Currency.USD));

        PriceListPosition position = new PriceListPosition(productDao.load(AppSettings.getPrimeProduct()), result);
        result.getPositions().add(position);

        return result;
    }

    private User getCurrentUser() {
        return userDao.load(principalHolder.getCurrentUser().getId());
    }

    public List<PriceList> findAllForCurrentUser() {
        return postLoad(getDao().findForUser(principalHolder.getCurrentUser().getId()));
    }

    //    todo test
    public PriceList getPriceList(long userId, int position) {
        return postLoad(getDao().load(new PriceListPK(userId, position)));
    }

    @Override
    protected PriceListPK getPK(PriceList entity) {
        return new PriceListPK(entity.getUser().getId(), entity.getPosition());
    }

    public PriceList findByPositionForCurrentUser(int position) {
        return postLoad(getDao().load(new PriceListPK(principalHolder.getCurrentUser().getId(), position)));
    }

    public List<PriceList> getAccessiblePriceLists() {
        User user = userService.loadCurrentUser();

        return dao.getAccessiblePriceLists(user.getId());
    }
}
