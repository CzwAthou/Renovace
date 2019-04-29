package com.renovace;

/**
 * @author athou
 * @date 2017/12/13
 */

public class RenovaceException extends RuntimeException {

    private int code;

    private RenovaceException() {
    }

    public RenovaceException(String detailMessage) {
        super(detailMessage);
    }

    public RenovaceException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}