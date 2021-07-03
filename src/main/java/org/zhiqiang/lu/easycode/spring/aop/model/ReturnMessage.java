package org.zhiqiang.lu.easycode.spring.aop.model;

import java.util.Date;

public class ReturnMessage {
    private int status;
    private Date timestamp;
    private String error;
    private String message;
    private String path;
    private Object data;
    private Boolean encryption = false;

    public ReturnMessage() {
        this.status = 200;
        this.timestamp = new Date();
        this.message = "OK";
        this.encryption = false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getEncryption() {
        return encryption;
    }

    public void setEncryption(Boolean encryption) {
        this.encryption = encryption;
    }
}
