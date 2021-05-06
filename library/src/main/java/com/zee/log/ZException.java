package com.zee.log;

import com.zee.log.ExceptionFileUtil;

public class ZException extends RuntimeException {

    public ZException() {
        super();
    }

    public ZException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);

        ExceptionFileUtil.dealNativeWithException(detailMessage + "\r\n");
    }

    public ZException(Exception e) {
        this(e, "");
    }

    public ZException(String detailMessage) {
        super(detailMessage);
        ExceptionFileUtil.dealNativeWithException(detailMessage + "\r\n");
    }

    public ZException(Exception e, String message) {
        super(e);
        ExceptionFileUtil.dealNativeWithException(e, message);
    }

}
