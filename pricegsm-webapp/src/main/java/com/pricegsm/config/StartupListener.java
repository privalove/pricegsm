package com.pricegsm.config;

import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Random;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        context.setAttribute(Constants.ASSETS_VERSION, RandomStringUtils.randomAlphanumeric(8));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
