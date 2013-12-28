package com.pricegsm.jackson;

import java.io.Serializable;

/**
 * Short version of Global Entity.
 */
public class GlobalEntityWrapper
        implements Serializable {

    private long id;

    private String name;

    public GlobalEntityWrapper() {
    }

    public GlobalEntityWrapper(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
