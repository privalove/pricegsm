package com.pricegsm.dao;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.DaoTest;
import com.pricegsm.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;

@DaoTest
@FlywayTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    /**
     * Readonly test no need @Transaction annotation
     */
    @Test
    public void testFindActive() throws Exception {
        List<User> users = userDao.findActive();

        //this great asset set default message for condition.
        assertThat(users.size()).isGreaterThanOrEqualTo(2);
    }

    /**
     * Update database method you should add @Transaction annotation
     */
    @Test
    @Transactional
    @Rollback(false)
    public void testPersist() throws Exception {
        User user = new User("Persisted User", "persisted@pricegsm.com", "12345678");
        user.setToken(UUID.randomUUID().toString().replaceAll("-", ""));

        userDao.persist(user);
    }

    /**
     * Method for verify @Transactional test
     *
     * Pay attention there is only one method is marked as @AfterTransaction in the test class.
     * So you have to create separate class for each test of update database
     */
    @AfterTransaction
    public void verifyPersist() throws Exception {
        User user = userDao.loadByEmail("persisted@pricegsm.com");

        //this great asset set default message for condition.
        assertThat(user).isNotNull();
    }

}
