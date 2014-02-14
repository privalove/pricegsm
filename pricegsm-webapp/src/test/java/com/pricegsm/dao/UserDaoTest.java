package com.pricegsm.dao;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.DaoTest;
import com.pricegsm.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
    public void testPersist() throws Exception {
        User user = new User("Persisted User", "persisted@pricegsm.com", "12345678");
        user.setToken(UUID.randomUUID().toString().replaceAll("-", ""));

        userDao.persist(user);

        user = userDao.loadByEmail("persisted@pricegsm.com");

        //this great asset set default message for condition.
        assertThat(user).isNotNull();

    }
}
