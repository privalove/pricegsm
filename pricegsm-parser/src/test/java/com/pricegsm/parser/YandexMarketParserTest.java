package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author andreybugaev
 * @date 2/1/13
 */
public class YandexMarketParserTest {

    private YandexMarketParser parser;

    @Before
    public void setUp() throws Exception {

        parser = new YandexMarketParser(){
            @Override
            protected HtmlPage getFirstPage(long yandexId, long yandexTypeId, int page, WebClient webClient) throws IOException {
                MockWebConnection connection = new MockWebConnection();
                String content = "";
                InputStream resourceAsStream = null;
                try {
                    resourceAsStream = getClass().getResourceAsStream("/yandex.html");
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

            @Override
            protected HtmlPage getSecondPage(long yandexId, long yandexTypeId, long shopId, WebClient webClient) throws IOException {
                MockWebConnection connection = new MockWebConnection();
                String content = "";
                InputStream resourceAsStream = null;
                try {
                    resourceAsStream = getClass().getResourceAsStream("/yandex2.html");
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
        String result = parser.parse(12345, 91491, "gray", "gold", "silver");
        Assert.assertTrue(result.contains("\"error\":0"));
        System.out.println(result);
    }
}
