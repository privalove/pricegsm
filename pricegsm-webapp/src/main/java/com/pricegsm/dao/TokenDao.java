package com.pricegsm.dao;

import com.pricegsm.domain.Token;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class TokenDao
        implements PersistentTokenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }


    @Transactional
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        getEntityManager().persist(new Token(token));
    }

    @Transactional
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        Token t = getEntityManager().find(Token.class, series);

        t.setTokenValue(tokenValue);
        t.setDate(lastUsed);

        getEntityManager().merge(t);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Token token = getEntityManager().find(Token.class, seriesId);
        return token != null ? token.toToken() : null;
    }

    @Transactional
    @Override
    public void removeUserTokens(String username) {
        List<Token> list = getEntityManager()
                .createQuery("select t from Token t where t.username = :username")
                .setParameter("username", username)
                .getResultList();

        for (Token token : list) {
            getEntityManager().remove(token);
        }
    }
}
