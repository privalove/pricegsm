package com.pricegsm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic operation result class, suitable for Ajax operations
 *
 * <code>
 * {
 *      ok: true             boolean value in case status.toLowerCase() == "ok"
 *      status: "OK"         string status, can be customized with OperationResult.build(String);
 *      payload: {           free form addition payload to return any required operation data
 *          ref: { .. }
 *      }
 * }
 * </code>
 */
public class OperationResult {
    private static final Logger log = LoggerFactory.getLogger(OperationResult.class);

    public enum Status {
        OK, FAIL, VALIDATION, EXCEPTION
    }

    private String status;

    private String message;

    private Map<String, Object> payload;

    public OperationResult(Status status, String message) {
        this.status = status.name();
        this.message = message;
        this.payload = new HashMap<>();
    }

    public OperationResult(String status, String message) {
        this.status = status;
        this.message = message;
        this.payload = new HashMap<>();
    }

    public String getStatus() {
        return status;
    }

    public boolean isOk() {
        return this.status.equalsIgnoreCase(Status.OK.name());
    }

    public void setStatus(Status status) {
        this.status = status.name();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public OperationResult payload(String key, Object value) {
        this.getPayload().put(key, value);
        return this;
    }

    public OperationResult message(String message) {
        this.setMessage(message);
        return this;
    }

    public static OperationResult ok() {
        return new OperationResult(Status.OK, null);
    }

    public static OperationResult fail() {
        return new OperationResult(Status.FAIL, null);
    }

    public static OperationResult validation() {
        return new OperationResult(Status.VALIDATION, null);
    }

    public static OperationResult fail(String message) {
        return new OperationResult(Status.FAIL, message);
    }

    public static OperationResult build(String status) {
        return new OperationResult(status, null);
    }

    public static OperationResult build(String status, String message) {
        return new OperationResult(status, message);
    }

    public static OperationResult fail(Throwable t) {
        log.error("Operation failed", t);
        OperationResult or = new OperationResult(Status.EXCEPTION, t.getMessage());

        // including the exception itself will cause stacktrace to be serialized, don't do this
        if (t.getCause() != null) {
            or.getPayload().put("cause", t.getCause().getMessage());
        }
        return or;
    }

    @Override
    public String toString() {
        return "OperationResult{" +
                "status=" + status +
                ((message == null || message.length() == 0)? ", no message" : ", message='" + message + '\'') +
                (payload.isEmpty() ? ", no" : ", with") + " payload" +
                '}';
    }

}