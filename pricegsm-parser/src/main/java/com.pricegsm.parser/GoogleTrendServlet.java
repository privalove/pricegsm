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
@WebServlet(name="googleServlet", urlPatterns={"/google"})
public class GoogleTrendServlet extends HttpServlet {

    private GoogleTrendsParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        parser = new GoogleTrendsParser();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String flagman = req.getParameter("flagman");
        String query = req.getParameter("query");

        String content = query != null ? parser.parse(flagman, query) : parser.parse(flagman);

        //resp.setContentLength(content.length());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(content);
    }
}
