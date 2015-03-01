package com.pricegsm.dao;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.DaoTest;
import com.pricegsm.domain.YandexPrice;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * User: o.logunov
 * Date: 21.02.15
 * Time: 17:33
 */
@DaoTest
@FlywayTest
@RunWith(SpringJUnit4ClassRunner.class)
public class YandexPriceDaoTest extends TestCase {

    @Autowired
    private YandexPriceDao yandexPriceDao;

    @Test
    public void testFindLastMinPrice() {
        // Given
        // When
        // Then
        assertEquals(5, yandexPriceDao.findLastMinPrice(2001L).getId());
        assertEquals(15, yandexPriceDao.findLastMinPrice(2002L).getId());
    }

    @Test
    public void testFindLastMinPrices() {
        // Given
        // When
        List<YandexPrice> byDateMinPrices =
                yandexPriceDao.findLastMinPrices(101L);

        // Then
        assertEquals(5L, byDateMinPrices.get(0).getId());
        assertEquals(15L, byDateMinPrices.get(1).getId());
    }
}
