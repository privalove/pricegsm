package com.pricegsm.parser;

import java.util.Arrays;
import java.util.List;

public final class Config {

    private static final List<Proxy> proxies = Arrays.asList(
            new Proxy("190.102.30.19", 80),
            new Proxy("216.83.154.14", 3128),
            new Proxy("193.254.236.201", 3128),
            new Proxy("80.193.214.230", 3128)
            );

    public static List<Proxy> getProxies() {
        return proxies;
    }

    public static class Proxy {
        private String host;

        private int port;

        public Proxy(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }
}
