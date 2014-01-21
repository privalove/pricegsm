package com.pricegsm.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

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

    public String parse(long yandexId, long yandexTypeId, Map<String, String> colors) {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setJavaScriptEnabled(false);

        UrlFetchUtil.setupCookies(webClient.getCookieManager());

        HtmlPage page;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            List<Position> positions = new ArrayList<>();


            for (int pageNumber = 1; pageNumber <= PAGES; pageNumber++) {
                page = getFirstPage(yandexId, yandexTypeId, pageNumber, webClient);


                JSONObject shops = UrlFetchUtil.processPage(page.asXml(), "/template/yandex.xsl");

                positions.addAll(
                        mapper.<Collection<? extends Position>>readValue(
                                shops.getJSONArray("offer").toString(),
                                new TypeReference<List<Position>>() {
                                }));

            }

            YandexMarketResult result = new YandexMarketResult();
            List<Position> offers = result.getResult().getOffers();


            int position = 0;
            int realPosition = 0;

            for (int i = 0; i < positions.size(); i++) {
                long shopId = positions.get(i).getShopId();

                List<Position> subPositions = new ArrayList<>();
                subPositions.add(positions.get(i));
                positions.get(i).setPosition(i);

                if (shopId > 0) {

                    page = getSecondPage(yandexId, yandexTypeId, shopId, webClient);

                    Map<String, Object> params = new HashMap<>();
                    params.put("position", i);

                    JSONObject offer = UrlFetchUtil.processPage(page.asXml(), "/template/yandex2.xsl", params);

                    subPositions.addAll(mapper.<Collection<Position>>readValue(
                            offer.getJSONArray("offer").toString(),
                            new TypeReference<List<Position>>() {
                            }));
                }


                label:
                for (Position shopOffer : subPositions) {
                    if (!StringUtils.isEmpty(shopOffer.getName())) {

                        for (Map.Entry<String, String> entry : colors.entrySet()) {
                            String pattern = ".*" + entry.getValue().replaceAll(",", ".*|.*") + ".*";
                            boolean find = Pattern.compile(pattern).matcher(shopOffer.getName()).find();
                            if (find) {

                                shopOffer.setColor(entry.getKey());

                                if (position < shopOffer.getPosition()) {
                                    realPosition += 1;
                                }

                                if (realPosition >= MAX_RESULT) {
                                    break;
                                }

                                position = shopOffer.getPosition();
                                shopOffer.setPosition(realPosition);

                                if (!StringUtils.isEmpty(shopOffer.getLink())) {
                                    try {
                                        HtmlPage shopPage = webClient.getPage(shopOffer.getLink());
                                        shopOffer.setLink(shopPage.getPage().getUrl().toString());
                                    } catch (Exception e) {
                                        logger.error("Error getting shop URL {}", Throwables.getRootCause(e).getMessage());
                                        logger.debug(Throwables.getRootCause(e).getMessage(), Throwables.getRootCause(e));
                                    }
                                }

                                offers.add(shopOffer);


                                continue label;
                            }
                        }
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
