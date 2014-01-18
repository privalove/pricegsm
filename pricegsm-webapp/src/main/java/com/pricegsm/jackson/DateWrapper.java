package com.pricegsm.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Wrapper to convert date to JSON String 'yyyy-MM-dd'
 */
@JsonSerialize(using = DateWrapperSerializer.class)
public class DateWrapper
        implements Serializable {

    private Date date;

    public DateWrapper(Date date) {
        this.date = date;
    }

    //todo don't work together in 2.3.0 version
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonValue
    public Date getDate() {
        return date;
    }
}
