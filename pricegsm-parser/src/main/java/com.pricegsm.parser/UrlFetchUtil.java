package com.pricegsm.parser;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class UrlFetchUtil {

    public static final MessageFormat ERROR = new MessageFormat("'{'\"result\":'{'\"error\":1, \"message\":\"{0}\"'}}'");

    private static final Logger logger = LoggerFactory.getLogger(UrlFetchUtil.class);

    public static String processXSL(String document, String template, Map<String, Object> params) throws TransformerException {
        InputStream templateIs = null;

        try {
            templateIs = UrlFetchUtil.class.getResourceAsStream(template);
            Transformer t = TransformerFactory
                    //XSLT 2.0
                    .newInstance("net.sf.saxon.TransformerFactoryImpl", null)
                    .newTransformer(new StreamSource(templateIs));

            Writer result = new StringWriter();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                t.setParameter(entry.getKey(), entry.getValue());
            }

            t.transform(new StreamSource(new StringReader(document)), new StreamResult(result));

            return result.toString();
        } finally {
            close(templateIs);
        }
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        //
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        //
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static JSONObject xmlTojson(String xml) throws JSONException {
        return XML.toJSONObject(xml);
    }

    public static JSONObject processPage(String page, String template)
            throws TransformerException, JSONException {

        return processPage(page, template, Collections.<String, Object>emptyMap());
    }

    public static JSONObject processPage(String page, String template, Map<String, Object> params)
            throws TransformerException, JSONException {

        String xml = processXSL(page, template, params);

        return xmlTojson(xml);
    }

    public static void close(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
    }

    public static void setupCookies(CookieManager cookieManager) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(2023, 0, 31, 7, 27, 0);
        Date expires = c.getTime();

        cookieManager.addCookie(new Cookie(".google.com", "APISID", "a-yPKG8xpJeUvmCY/AhoMeAMoP_qkMEesM", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "HSID", "AuVmBXrPiji1L4uAI", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "NID", "67=Z5yyl1tXbEr-vGmWWfq7jriKiBJcwsMviN0tIpJnKF_KxnCKD7dS_kNEygD-Nd2mzC7lXePmYfI9f-ajdcRE8fJGXai6M2r45MnYLs2WjWC9CC6YS82h0ia2g_iUGgRZNRc-wLS1N-dQeZvOSUMrV7njlYtDk1Jg_jQGnuGUQ_Np", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "PREF", "ID=ad0970d669b60981:U=32c2275223a47f21:FF=0:LD=en:TM=1359788528:LM=1359790250:GM=1:S=UIlGeK1uyqQn_CXQ", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "S", "izeitgeist-ad-metrics=EtXnGVoSFao", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "SAPISID", "HErIXo952WHe1p2g/AgyVQszB7jom4KpLD", "/", expires, true, false));
        cookieManager.addCookie(new Cookie(".google.com", "SID", "DQAAALAAAAAvAEKG73M9wODTYLTlV1ORBIXYAE56X75NHLLHNfCYE1CIvMI2Q36Q5UzCEejtvG6Z4-qCHK2bENYEdyJBKlcBCTkxbJp4IF7NT5aNbJ5GJEd9VPGzkG5IzjcCUrqcggdgyyu0hJPYqCfqzsSWlhCLZJJHHE8lQGw_JQ3AM-Zbm4pBFX8HLDAet3in7zZDwoxURazKlo41sc4RoQ7E4gB0km1Yqok3w5NlsWW4eEoSSQ", "/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "SNID", "67=xYZyKJS1XTDbN4VJ6rNDOCFii4aCmZyYDTsx0d0n=uILZOfJprPUibTNw", "/verify", expires, true, true));
        cookieManager.addCookie(new Cookie(".google.com", "SSID", "AJYGwyGyAsBGfKqAq", "/", expires, true, true));
        cookieManager.addCookie(new Cookie("www.google.com", "I4SUserLocale", "en_US", "/trends", expires, false, true));

        cookieManager.addCookie(new Cookie(".yandex.ru", "yandex_gid", "213", "/", expires, false, false));


        /*
        cookieManager.addCookie(new Cookie(".google.com", "__utma", "173272373.457689439.1359787818.1359787818.1359789917.2", "/trends", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utma", "173272373.457689439.1359787818.1359787818.1359789917.2", "/trends/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmb", "173272373.5.10.1359789917", "/trends", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmb", "173272373.5.10.1359789917", "/trends/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmc", "173272373", "/trends", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmc", "173272373", "/trends/", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmz", "173272373.1359787818.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)", "/trends", expires, false, false));
        cookieManager.addCookie(new Cookie(".google.com", "__utmz", "173272373.1359787818.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)", "/trends/", expires, false, false));
        */
    }
}
