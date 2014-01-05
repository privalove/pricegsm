package com.pricegsm.parser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author andreybugaev
 * @date 2/2/13
 */
@WebServlet(name="yandexServlet", urlPatterns={"/yandex"})
public class YandexMarketServlet extends HttpServlet {

    private YandexMarketParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        parser = new YandexMarketParser();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long yandexId = Long.parseLong(req.getParameter("yandexId"));
        long yandexTypeId;
        try {
            yandexTypeId = Long.parseLong(req.getParameter("yandexTypeId"));
        } catch (NumberFormatException e) {
            //mobile phones
            yandexTypeId = 91491;
        }
        String[] colors = req.getParameterValues("color");
        String content = parser.parse(yandexId, yandexTypeId, colors);

        //resp.setContentLength(content.length());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(content);
    }
}
