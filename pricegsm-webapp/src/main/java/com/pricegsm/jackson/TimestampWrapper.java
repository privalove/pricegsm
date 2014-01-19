package com.pricegsm.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Wrapper to convert date to JSON String 'yyyy-MM-dd HH:mm:ss'
 */
@JsonSerialize(using = TimestampWrapperSerializer.class)
public class TimestampWrapper
        implements Serializable {

    private Date date;

    public TimestampWrapper(Date date) {
        this.date = date;
    }

    //todo don't work together in 2.3.0 version
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonValue
    public Date getDate() {
        return date;
    }
}
