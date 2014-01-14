package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andreybugaev
 * @date 2/1/13
 */
public class YandexMarketParser {

    public static final String template = "http://market.yandex.ru/offers.xml?modelid={0}&hid={1}&grhow=shop&how=aprice&np=1&page={2}&onstock=1";

    public static final String template2 = "http://market.yandex.ru/offers.xml?modelid={0}&hid={1}&how=aprice&np=1&hideduplicate=0&fesh={2}";

    public static final int MAX_RESULT = 10;

    public static final int PAGES = 2;

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected HtmlPage getFirstPage(long yandexId, long yandexTypeId, int page, WebClient webClient) throws IOException {
        return webClient.getPage(new MessageFormat(template).format(new Object[]{String.valueOf(yandexId), String.valueOf(yandexTypeId), page}));
    }

    protected HtmlPage getSecondPage(long yandexId, long yandexTypeId, long shopId, WebClient webClient) throws IOException {
        return webClient.getPage(new MessageFormat(template2).format(new Object[]{String.valueOf(yandexId), String.valueOf(yandexTypeId), String.valueOf(shopId)}));
    }

    public String parse(long yandexId, long yandexTypeId, String... colors) {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setJavaScriptEnabled(false);

        UrlFetchUtil.setupCookies(webClient.getCookieManager());

        HtmlPage page;
        try {

            String colorPattern = "";

            for (int i = 0; i < colors.length; i++) {
                String color = colors[i];
                colorPattern += ".*(" + color + ").*";
                if (i < (colors.length - 1)) {
                    colorPattern += "|";
                }
            }
            colorPattern = colorPattern.toLowerCase();

            List<Long> shopIds = new ArrayList<>();

            for (int pageNumber = 1; pageNumber <= PAGES; pageNumber++) {
                page = getFirstPage(yandexId, yandexTypeId, pageNumber, webClient);


                JSONObject shops = UrlFetchUtil.processPage(page.asXml(), "/template/yandex.xsl");
                JSONArray _shopIds = shops.getJSONObject("result").getJSONArray("link");

                for (int i = 0; i < _shopIds.length(); i++) {
                    shopIds.add(_shopIds.getLong(i));
                }
            }

            JSONObject result = new JSONObject();
            JSONObject data = new JSONObject();
            JSONArray offers = new JSONArray();
            data.put("error", 0);
            data.put("offers", offers);
            result.put("result", data);


            int position = 0;
            int realPosition = 0;

            for (int i = 0; i < shopIds.size(); i++) {
                long shopId = shopIds.get(i);
                page = getSecondPage(yandexId, yandexTypeId, shopId, webClient);

                Map<String, Object> params = new HashMap<>();
                params.put("colorPattern", colorPattern);
                params.put("position", i);

                JSONObject offer = UrlFetchUtil.processPage(page.asXml(), "/template/yandex2.xsl", params);
                JSONArray shopOffers = offer.getJSONArray("offer");


                for (int j = 0; j < shopOffers.length(); j++) {
                    JSONObject shopOffer = shopOffers.getJSONObject(j);
                    if (shopOffer.getString("color").length() > 0) {

                        if (position < shopOffer.getInt("position")) {
                            realPosition += 1;
                        }

                        if (realPosition >= MAX_RESULT) {
                            break;
                        }

                        position = shopOffer.getInt("position");
                        shopOffer.put("position", realPosition);

                        offers.put(shopOffer);
                    }
                }
            }


            return result.toString();

        } catch (JSONException e) {
            logger.error("Error parsing json");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error parsing json"});
        } catch (TransformerException e) {
            logger.error("Error parsing xml");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error parsing xml"});
        } catch (Exception e) {
            logger.error("Error fetching url");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error fetching url"});
        }
    }


}
