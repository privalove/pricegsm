package com.pricegsm.dao;

import com.pricegsm.domain.Currency;
import com.pricegsm.domain.YandexPrice;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Repository
public class YandexPriceDao
        extends GlobalEntityDao<YandexPrice> {

    /**
     * @return List of pairs {Product Yandex ID, Date}
     */
    public List<Object[]> findLast() {
        return getEntityManager()
                .createQuery("select p.yandexId, max(y.date) from YandexPrice as y right outer join y.product as p "
                        + " where p.active = true group by p.yandexId order by max(y.date)")
                .getResultList();
    }

    /**
     * @return List of pairs {Product ID, Date}
     */
    public List<Object[]> findLastByColors() {
        return getEntityManager()
                .createQuery("select p.id, max(y.date) from YandexPrice as y right outer join y.product as p "
                        + " where p.active = true group by p.id order by max(y.date)")
                .getResultList();
    }

    public Date findLastDate(long productId) {
        return (Date) getEntityManager()
                .createQuery("select max(y.date) from YandexPrice y where y.product.id = :productId")
                .setParameter("productId", productId)
                .getSingleResult();
    }

    public Date findLastDate(long productId, Date from, Date to) {
        return (Date) getEntityManager()
                .createQuery("select max(y.date) from YandexPrice y where y.product.id = :productId and y.date <= :to and y.date >= :from")
                .setParameter("productId", productId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }

    public YandexPrice findLastMinPrice(long productId) {
        try {
            return (YandexPrice) getEntityManager()
                    .createQuery("select y from YandexPrice y where y.product.id = :productId order by y.date desc, y.priceRub asc")
                            //skip min price for statistic
                    .setFirstResult(1)
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<YandexPrice> findLastMinPrices(Long vendorId) {
        try {
            List vendorId1 = getEntityManager()
                    .createNativeQuery(
                            "select y.* from {h-schema}yandex_price y "
                                    + " inner join {h-schema}product pr on y.product_id = pr.id "
                                    + " where pr.vendor_id = :vendorId "
                                    + " and y.id = (select y1.id from {h-schema}yandex_price y1 "
                                    + " where pr.id = y1.product_id "
                                    + " order by y1.sell_date desc, y1.price_rub asc "
                                    + " offset 1 limit 1)",
                            YandexPrice.class
                    ).setParameter("vendorId", vendorId)
                    .getResultList();
            return (List<YandexPrice>) vendorId1;
        } catch (NoResultException e) {
            return null;
        }
    }

    public YandexPrice findByDateMinPrice(long productId, Date date) {
        try {
            return (YandexPrice) getEntityManager()
                    .createQuery("select y from YandexPrice y where y.product.id = :productId and y.date <= :date order by y.date desc, y.priceRub asc")
                            //skip min price for statistic
                    .setFirstResult(1)
                    .setMaxResults(1)
                    .setParameter("productId", productId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<YandexPrice> findByDateForProduct(long productId, Date from, Date to) {
        return getEntityManager()
                .createQuery("select y from YandexPrice y where y.product.id = :product and y.date = (select max(date) from YandexPrice where product.id = :product and date <= :to and date >= :from) order by y.priceRub")
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("product", productId)
                .getResultList();
    }

    public List<Object[]> getChartData(long productId, int currency, Date from, Date to) {

        String price;

        switch (currency) {
            case (int) Currency.EUR:
                price = "price_eur";
                break;
            case (int) Currency.USD:
                price = "price_usd";
                break;
            default:
                price = "price_rub";
        }

        return getEntityManager()
                //skip min price for statistic
                .createNativeQuery("select sell_date, avg(price) from "
                        + " (select y.sell_date as sell_date, y." + price + " as price, rank() over (partition by y.sell_date order by y.sell_date, y." + price + " asc) as rank from yandex_price y "
                        + "      where y.product_id =:productId and y.sell_date >= :from and y.sell_date <= :to) x "
                        + " where rank in (2,3,4) group by sell_date")

                .setParameter("productId", productId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}
