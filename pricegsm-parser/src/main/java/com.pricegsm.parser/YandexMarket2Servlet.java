package com.pricegsm.parser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="yandex2Servlet", urlPatterns={"/yandex2"})
public class YandexMarket2Servlet extends HttpServlet {

    private YandexMarketAPIParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        parser = new YandexMarketAPIParser();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        long yandexTypeId = Long.parseLong(req.getParameter("yandexTypeId"));
        boolean manufacturerWarranty = Boolean.parseBoolean(req.getParameter("manufacturerWarranty"));


        String content = parser.parse(query, yandexTypeId, manufacturerWarranty);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(content);
    }
}
