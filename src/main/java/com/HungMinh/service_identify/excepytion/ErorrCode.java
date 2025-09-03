package com.HungMinh.service_identify.excepytion;

public enum ErorrCode {
    UN_Loi( 9999,  "User Loi"),
    USER_EXISTED( 1002,  "User existed"),
    VALID_CHECK (1003,"Toi thieu 8 ki tu"),
    KO_THAY(2000,"Ko thay hang so ")
    ;

    ErorrCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }



}
