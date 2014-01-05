package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author andreybugaev
 * @date 2/1/13
 */
public class GoogleTrendsParser {

    public static final String template = "http://www.google.com/trends/trendsReport?hl=en-US&q={1},{0}&geo=RU&date=today+7-d&cmpt=q&content=1";


    public static final String year = "http://www.google.com/trends/trendsReport?hl=en-US&q={0}&geo=RU&date=today+12-m&cmpt=q&content=1";


    public static final String MAGIC_PATTERN = "\\[[\\W]*\"Average\"[\\W,]+([\\d]+[\\W,\\d]+[\\d]+)\\]";

    public static final String MAGIC_PATTERN_2 = "\\[\\{\"f\"\\:\"[^\"]+\",\"v\"\\:new Date\\((\\d+), (\\d+), (\\d+)\\)\\},null,null,(\\d+),[^]]+\\]";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected HtmlPage getPage(String template, String flagman, String query, WebClient webClient) throws IOException {
        String url = new MessageFormat(template).format(new Object[]{
                URLEncoder.encode(flagman, "UTF-8"),
                URLEncoder.encode(query, "UTF-8")});

        logger.warn("fetch url {}", url);

        WebRequest request = new WebRequest(new URL(url));
        request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        request.setAdditionalHeader("Accept-Language", "en-US,en;q=0.5");
        request.setAdditionalHeader("Connection", "keep-alive");
        request.setAdditionalHeader("Host", "www.google.com");

        UrlFetchUtil.setupCookies(webClient.getCookieManager());

        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:18.0) Gecko/20100101 Firefox/18.0");

        return webClient.getPage(request);
    }

    public String parse(String flagman) {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page;
        try {
            page = getPage(year, flagman, "", webClient);


            String text = page.asXml();

            Pattern pattern = Pattern.compile(MAGIC_PATTERN_2);
            Matcher m = pattern.matcher(text);
            int index = 0;
            String result = "";
            while (m.find()) {

                if (index++ > 0) {
                    result += ",";
                }

                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), 0, 0, 0);

                result += "{\"date\":\"" + DATE_FORMAT.format(c.getTime()) + "\", \"time\":" + c.getTimeInMillis() + ", \"value\":" + m.group(4) + "}";
            }
            if (index > 0) {
                return "{\"result\":{\"error\":0, \"data\":[" + result + "]}}";
            } else {
                return UrlFetchUtil.ERROR.format(new Object[]{"Data not found"});
            }

        } catch (Exception e) {
            logger.error("Error fetching url");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error fetching url " + e.getMessage()});
        }

    }

    public String parse(String flagman, String query) {

        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page;
        try {
            page = getPage(template, flagman, query, webClient);


            String text = page.asXml();

            Pattern pattern = Pattern.compile(MAGIC_PATTERN);
            Matcher m = pattern.matcher(text);

            if (m.find()) {
                String[] values = m.group(1).split("[\\W,]+");
                int result = 0;
                for (int i = 0; i < values.length - 1; i++) {
                    result += Integer.parseInt(values[i]);
                }


                return "{\"result\":{\"error\":0, \"current\":" + result + ", \"flagman\":" + values[values.length - 1] + "}}";
            } else {
                return UrlFetchUtil.ERROR.format(new Object[]{"Data not found"});
            }

        } catch (Exception e) {
            logger.error("Error fetching url");
            return UrlFetchUtil.ERROR.format(new Object[]{"Error fetching url " + e.getMessage()});
        }
    }

}
