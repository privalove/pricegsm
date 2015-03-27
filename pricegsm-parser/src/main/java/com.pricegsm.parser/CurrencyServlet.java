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
@WebServlet(name="currencyServlet", urlPatterns={"/currency"})
public class CurrencyServlet extends HttpServlet {

    private CurrencyParserYahooapis parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        parser = new CurrencyParserYahooapis();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String content = parser.parse();

        //resp.setContentLength(content.length());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(content);
    }
}
