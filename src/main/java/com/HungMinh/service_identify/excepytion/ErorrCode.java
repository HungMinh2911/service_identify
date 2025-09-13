package com.HungMinh.service_identify.excepytion;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErorrCode {
    UN_Loi( 9999,  "User Loi", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"Uncategorized",HttpStatus.BAD_REQUEST),
    USER_EXISTED( 1002,  "User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID (1003,"Toi thieu 8 ki tu", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD  (1004,"Ko thay hang so ",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED (1005, "Khong tim thay user",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED (1006,"UNAUTHENTICATED",HttpStatus.UNAUTHORIZED),
    UNAUTHORTIZED (1007,"You do not have permison",HttpStatus.FORBIDDEN),
    ;

    ErorrCode(int code, String message,HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;




}
