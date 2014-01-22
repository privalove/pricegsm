package com.pricegsm.dao;

import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Product;
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

    public List<YandexPrice> findByDateForProducts(Date date, List<Product> products) {
        return getEntityManager()
                .createQuery("select y from YandexPrice y where y.product in :products and y.date = (select max(date) from YandexPrice where date <= :date) order by y.position, y.shop")
                .setParameter("date", date)
                .setParameter("products", products)
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
                .createNativeQuery("select sell_date, price from "
                        + " (select y.sell_date as sell_date, y." + price + " as price, rank() over (partition by y.sell_date order by y.sell_date, y." + price + " asc) as rank from yandex_price y "
                        + "      where y.product_id =:productId and y.sell_date >= :from and y.sell_date <= :to) x "
                        + " where rank = 2")

                .setParameter("productId", productId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}
