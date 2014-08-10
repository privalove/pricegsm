package com.pricegsm.dao;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.DaoTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: o.logunov
 * Date: 09.08.14
 * Time: 0:54
 */
@DaoTest
@FlywayTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderDaoTest {

    @Autowired
    OrderDao orderDao;

    @Test
    public void testFindByUserIdOrderId() {
        assertThat(orderDao.findByUserIdOrderId(10001L, 1001L).getId()).isEqualTo(1001L);
    }
}
