package com.HungMinh.service_identify.excepytion;


import com.HungMinh.service_identify.dto.request.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHander {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<String> handlingRuntimeException1(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
  }

//    ResponseEntity<APIResponse> handlingRuntimeException2(RuntimeException exception){
//        APIResponse apiResponse = new APIResponse();
//        apiResponse.setCode(ErorrCode.UN_Loi.getCode());
//        apiResponse.setMessage(ErorrCode.UN_Loi.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handlingAppException(AppException exception){
        ErorrCode erorrCode = exception.getErorrCode();

        APIResponse t = new APIResponse();
        t.setCode(erorrCode.getCode());
        t.setMessage(erorrCode.getMessage());
        return ResponseEntity.badRequest().body(t);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingValidation(MethodArgumentNotValidException  exception){
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErorrCode erorrCode = ErorrCode.KO_THAY;

        try{
            erorrCode = ErorrCode.valueOf(enumKey);
        }
        catch (IllegalArgumentException e){

        }

        APIResponse t = new APIResponse();
        t.setCode(erorrCode.getCode());
        t.setMessage(erorrCode.getMessage());

        return ResponseEntity.badRequest().body(t);
    }

}
