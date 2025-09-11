package com.HungMinh.service_identify.controller;


import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.AuthenticationResquest;
import com.HungMinh.service_identify.dto.request.IntrospectResquest;
import com.HungMinh.service_identify.dto.response.AuthenticationReponse;
import com.HungMinh.service_identify.dto.response.IntrospectReponse;
import com.HungMinh.service_identify.service.AuthenticationServer;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthenticationController {
    AuthenticationServer authenticationServer;
    @PostMapping("/token")
    APIResponse<AuthenticationReponse> authenticationReponseAPIResponse (@RequestBody AuthenticationResquest resquest) throws JOSEException {
        var result = authenticationServer.authenticate(resquest);

        return  APIResponse.<AuthenticationReponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspeact")
    APIResponse<IntrospectReponse> authenticate (@RequestBody IntrospectResquest resquest)
            throws ParseException, JOSEException {
        var result = authenticationServer.introspect(resquest);

        return  APIResponse.<IntrospectReponse>builder()
                .result(result)
                .build();
    }
}
