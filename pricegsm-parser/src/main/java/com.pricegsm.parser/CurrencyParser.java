package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author andreybugaev
 * @date 4/5/13
 */
public class CurrencyParser {

    public static final String URL = "http://www.micex.ru/markets/currency/today";

    public static final Map<String, String> MAPPING = new HashMap<String, String>() {{
        put("USD000000TOD", "USD_RUB");
        put("BKTRUB000TOM", "USD_RUB_2");
        put("EUR_RUB__TOD", "EUR_RUB");
        put("EURUSD000TOD", "EUR_USD");
    }};

    /**
     * ID, VALUE, HOUR, MINUTE
     */
    public static final String MAGIC_PATTERN = "\\{\\\\\"SECID\\\\\"\\: \\\\\"([0-9A-Z_]+)\\\\\".*?\\\\\"LAST\\\\\"\\: ([0-9\\.\\,]+)\\,.*?\\\\\"UPDATETIME\\\\\"\\: \\\\\"(\\d+)\\:(\\d+)\\\\\"";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected HtmlPage getPage(WebClient webClient) throws IOException {
        return webClient.getPage(URL);
    }

    public String parse() {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page;
        try {
            page = getPage(webClient);

            String text = page.asXml();

            Pattern pattern = Pattern.compile(MAGIC_PATTERN);
            Matcher m = pattern.matcher(text);
            int index = 0;
            String result = "";
            while (m.find()) {

                if (index++ > 0) {
                    result += ",";
                }

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(3)));
                c.set(Calendar.MINUTE, Integer.parseInt(m.group(4)));

                result += "\"" + map(m.group(1)) + "\":{\"date\":\"" + DATE_FORMAT.format(c.getTime()) + "\", \"time\":" + c.getTimeInMillis() + ", \"value\":" + m.group(2) + "}";
            }
            if (index > 0) {
                return "{\"result\":{\"error\":0, \"data\":{" + result + "}}}";
            } else {
                return UrlFetchUtil.ERROR.format(new Object[]{"Data not found"});
            }


        } catch (Exception e) {
            logger.error("Error fetching url");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error fetching url " + e.getMessage()});
        }
    }

    private String map(String code) {
        return MAPPING.containsKey(code) ? MAPPING.get(code) : code;
    }
}
