package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class YandexMarket2ParserTest {

    private YandexMarket2Parser parser;

    @Before
    public void setUp() throws Exception {

        parser = new YandexMarket2Parser(){
            @Override
            protected HtmlPage getFirstPage(String query, long yandexTypeId, WebClient webClient) throws IOException {
                MockWebConnection connection = new MockWebConnection();
                String content = "";
                InputStream resourceAsStream = null;
                try {
                    resourceAsStream = getClass().getResourceAsStream("/yandex3.html");
                    content = UrlFetchUtil.convertStreamToString(resourceAsStream);
                } catch (Exception e) {
                    UrlFetchUtil.close(resourceAsStream);
                }
                URL fakeUrl = new URL("http://notfoundmegaurl.com/");
                connection.setResponse(fakeUrl, content);
                connection.setDefaultResponse("");
                webClient.setWebConnection(connection);
                webClient.getOptions().setJavaScriptEnabled(false);

                return webClient.getPage(fakeUrl);
            }
        };

    }

    @Test
    public void test() throws Exception {

        String result = parser.parse("iphone 5s 16gb", 91491);
        Assert.assertTrue(result.contains("\"error\":0"));
        System.out.println(result);
    }
}
