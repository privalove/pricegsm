package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author andreybugaev
 * @date 4/5/13
 */
public class CurrencyParserYahooapis {

    public static final String URL = "https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+%22USDRUB,EURRUB,EURUSD%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected UnexpectedPage getPage(WebClient webClient) throws IOException {
        return webClient.getPage(URL);
    }

    public static final Map<String, String> MAPPING = new HashMap<String, String>() {{
        put("USDRUB", "USD_RUB");
        put("EURRUB", "EUR_RUB");
        put("EURUSD", "EUR_USD");
    }};

    public String parse() {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getOptions().setJavaScriptEnabled(false);
        UnexpectedPage page;
        try {
            page = getPage(webClient);

            String content = getContent(page);
            JSONObject json = new JSONObject(content);

            int index = 0;
            String result = "";

            JSONArray rate = json.getJSONObject("query").getJSONObject("results").getJSONArray("rate");
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < rate.length(); i++) {
                JSONObject jsonPair = (JSONObject) rate.get(i);

                if (index++ > 0) {
                    result += ",";
                }

                result += "\"" + MAPPING.get(jsonPair.getString("id")) + "\":{\"date\":\"" + DATE_FORMAT.format(calendar.getTime()) + "\", \"time\":" + calendar.getTimeInMillis() + ", \"value\":" + jsonPair.getString("Rate") + "}";
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

    private String getContent(UnexpectedPage page) throws IOException {
        InputStream inputStream = page.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
            System.out.println(line);
        }
        inputStream.close();

        return sb.toString();
    }
}