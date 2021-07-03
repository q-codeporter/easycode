package org.zhiqiang.lu.easycode.spring.aop.model;

public class MissingParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingParameterException(String message) {
        super(message);
    }
}
