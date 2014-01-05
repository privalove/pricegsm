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
public class GoogleTrendsParserTest {

    private TestGoogleTrendsParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new TestGoogleTrendsParser();
    }

    @Test
    public void test() throws Exception {
        String result = parser.parse("","");
        Assert.assertTrue(result.contains("\"error\":0"));
        System.out.println(result);
    }

    @Test
    public void testYear() throws Exception {
        parser.testPage = "/googleyear.html";
        String result = parser.parse("");
        Assert.assertTrue(result.contains("\"error\":0"));
        System.out.println(result);
    }

    private class TestGoogleTrendsParser extends GoogleTrendsParser {

        private String testPage =  "/google.html";

        @Override
        protected HtmlPage getPage(String template, String flagman, String query, WebClient webClient) throws IOException {
            MockWebConnection connection = new MockWebConnection();
            String content = "";
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = getClass().getResourceAsStream(testPage);
                content = UrlFetchUtil.convertStreamToString(resourceAsStream);
            } catch (Exception e) {
                UrlFetchUtil.close(resourceAsStream);
            }
            URL fakeUrl = new URL("http://notfoundmegaurl.com/");
            connection.setResponse(fakeUrl, content);
            connection.setDefaultResponse("");
            webClient.setWebConnection(connection);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            return webClient.getPage(fakeUrl);
        }
    }
}
