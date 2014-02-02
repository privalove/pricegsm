package com.pricegsm.support.web;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;

/**
 * A message to be displayed in web context. Depending on the type, different style will be applied.
 */
public class Message {
    /**
     * Name of the flash attribute.
     */
    public static final String MESSAGE_ATTRIBUTE = "message";

    /**
     * The type of the message to be displayed. The type is used to show message in a different style.
     */
    public static enum Type {
        DANGER, WARNING, INFO, SUCCESS;
    }

    private final String message;
    private final Type type;
    private final Object[] args;

    public Message(String message, Type type) {
        this.message = message;
        this.type = type;
        this.args = null;
    }

    public Message(String message, Type type, Object... args) {
        this.message = message;
        this.type = type;
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public Object[] getArgs() {
        return args;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message that = (Message) o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getType(), that.getType());
        builder.append(getMessage(), that.getMessage());
        builder.append(getArgs(), that.getArgs());

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getType())
                .append(getMessage())
                .append(getArgs())
                .toHashCode();
    }
}
