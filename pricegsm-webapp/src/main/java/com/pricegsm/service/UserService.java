package com.pricegsm.service;

import com.pricegsm.controller.ProfileForm;
import com.pricegsm.dao.RegionDao;
import com.pricegsm.dao.UserDao;
import com.pricegsm.domain.Region;
import com.pricegsm.domain.User;
import com.pricegsm.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService
        extends GlobalEntityService<User> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDao dao;

    @Autowired
    private RegionDao regionDao;

    @Override
    protected UserDao getDao() {
        return dao;
    }

    @Override
    protected User preSave(User entity) {
        if (!Utils.isEmpty(entity.getPassword())) {
            entity.setPassword(encoder.encode(entity.getPassword()));
        }

        return super.preSave(entity);
    }

    @Override
    protected void merge(User persisted, User entity) {
        super.merge(persisted, entity);

        entity.setPassword(persisted.getPassword());
        entity.setBalance(persisted.getBalance());
        entity.setToken(persisted.getToken());
        entity.setEmailValid(persisted.isEmailValid());
    }

    /**
     * Prepare default user settings before registration.
     */
    @Override
    public User getDefaultInstance() {
        User user = super.getDefaultInstance();
        user.setRegion(regionDao.load(Region.MOSCOW));
        user.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        return user;
    }

    @Transactional
    public User updateProfile(ProfileForm profilerForm) {
        User user = getDao().load(principalHolder.getCurrentUser().getId());
        profilerForm.fill(user);
        return getDao().merge(user);
    }

    @Transactional
    public User changePassword(String password) {
        User user = getDao().load(principalHolder.getCurrentUser().getId());
        user.setPassword(encoder.encode(password));
        return getDao().merge(user);
    }
}
