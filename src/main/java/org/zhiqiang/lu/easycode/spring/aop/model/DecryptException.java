package org.zhiqiang.lu.easycode.spring.aop.model;

import java.io.IOException;

public class DecryptException extends IOException {

    private static final long serialVersionUID = 1L;

    public DecryptException() {
        super();
    }

    public DecryptException(String message) {
        super(message);
    }
}
