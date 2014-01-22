package com.pricegsm.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

public class YandexMarket2Parser {

    public static final String template = "http://market.yandex.ru/search.xml?hid={0}&text={1}&how=aprice&onstock=1";

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected HtmlPage getFirstPage(String query, long yandexTypeId, WebClient webClient) throws IOException {
        return webClient.getPage(new MessageFormat(template).format(new Object[]{String.valueOf(yandexTypeId), URLEncoder.encode(query, "UTF-8")}));
    }

    public String parse(String query, long yandexTypeId) {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setJavaScriptEnabled(false);

        UrlFetchUtil.setupCookies(webClient.getCookieManager());

        HtmlPage page;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            YandexMarketResult result = new YandexMarketResult();
            List<Position> positions = result.getResult().getOffers();


            page = getFirstPage(query, yandexTypeId, webClient);


            JSONObject shops = UrlFetchUtil.processPage(page.asXml(), "/template/yandex3.xsl");

            positions.addAll(
                    mapper.<Collection<? extends Position>>readValue(
                            shops.getJSONArray("offer").toString(),
                            new TypeReference<List<Position>>() {}));

            for (int i = 0; i < positions.size(); i++) {
                Position shopOffer = positions.get(i);
                shopOffer.setPosition(i);

                if (!StringUtils.isEmpty(shopOffer.getLink())) {
                    try {
                        HtmlPage shopPage = webClient.getPage(shopOffer.getLink());
                        shopOffer.setLink(shopPage.getPage().getUrl().toString());
                    } catch (Exception e) {
                        logger.error("Error getting shop URL {}", Throwables.getRootCause(e).getMessage());
                        logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
                    }
                }
            }


            return mapper.writeValueAsString(result);

        } catch (JSONException e) {
            logger.error("Error parsing json {}", Throwables.getRootCause(e).getMessage());
            logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
            return UrlFetchUtil.ERROR.format(new Object[]{"Error parsing json"});
        } catch (TransformerException e) {
            logger.error("Error parsing xml {}", Throwables.getRootCause(e).getMessage());
            logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
            return UrlFetchUtil.ERROR.format(new Object[]{"Error parsing xml"});
        } catch (Exception e) {
            logger.error("Error fetching url {}", Throwables.getRootCause(e).getMessage());
            logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
            return UrlFetchUtil.ERROR.format(new Object[]{"Error fetching url"});
        }
    }


}
