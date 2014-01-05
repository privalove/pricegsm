package com.pricegsm.service;

import com.pricegsm.dao.CurrencyDao;
import com.pricegsm.dao.ExchangeDao;
import com.pricegsm.domain.AppSettings;
import com.pricegsm.domain.Currency;
import com.pricegsm.domain.Exchange;
import com.pricegsm.util.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExchangeService
        extends GlobalEntityService<Exchange> {

    @Autowired
    private ExchangeDao dao;

    @Autowired
    private CurrencyDao currencyDao;

    @Override
    protected ExchangeDao getDao() {
        return dao;
    }

    //every day at 10:00, 14:00, 18:00
    @Scheduled(cron = "0 56,57,58 9,13,17 * * ?")
    @Transactional
    public void readExchanges() throws IOException {

        Exchange last = getDao().getLast(Currency.USD, Currency.RUB);
        Currency usd = currencyDao.load(Currency.USD);
        Currency eur = currencyDao.load(Currency.EUR);
        Currency rub = currencyDao.load(Currency.RUB);

        String url = AppSettings.getParserUrl() + "/currency";

        logger.info("Fetch url: {}", url);


        JSONObject response = Utils.readJsonFromUrl(url);

        JSONObject result = response.getJSONObject("result");

        if (result != null) {

            int error = result.getInt("error");

            if (error == 0) {
                JSONObject data = result.getJSONObject("data");


                Date date = new Date(data.getJSONObject("USD_RUB").getLong("time"));

                if (last == null || date.after(last.getDate())) {

                    double usdRate = data.getJSONObject("USD_RUB").getDouble("value");
                    double eurRate = data.getJSONObject("EUR_RUB").getDouble("value");

                    save(new Exchange(usd, rub, date, new BigDecimal(usdRate)));
                    save(new Exchange(eur, rub, date, new BigDecimal(eurRate)));
                    save(new Exchange(eur, usd, date, new BigDecimal(eurRate / usdRate)));
                    logger.info("Exchanges have been saved successfully");
                }
            }
        } else {
            logger.warn("Error fetching data");
        }

    }
}

