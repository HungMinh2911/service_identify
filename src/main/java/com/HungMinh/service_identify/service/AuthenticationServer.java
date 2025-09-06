package com.HungMinh.service_identify.service;

import com.HungMinh.service_identify.dto.request.AuthenticationResquest;
import com.HungMinh.service_identify.dto.request.IntrospectResquest;
import com.HungMinh.service_identify.dto.response.AuthenticationReponse;
import com.HungMinh.service_identify.dto.response.IntrospectReponse;
import com.HungMinh.service_identify.excepytion.AppException;
import com.HungMinh.service_identify.excepytion.ErorrCode;
import com.HungMinh.service_identify.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServer {
    UserRepository userRepository;

    @NonFinal
    // secret key chuoi bi mat de ky token
    @Value("${jwt.signerKey}")
    protected  String SIGNER_KEY ;

    public IntrospectReponse introspect (IntrospectResquest resquest) throws JOSEException, ParseException {
        var token = resquest.getToken();

        // tao 1 verifier de xac thuc chu ky token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // tach chuoi token tu do co the signedJWT.getHeader() signedJWT.getJWTClaimsSet() signedJWT.getSignature()
        SignedJWT signedJWT = SignedJWT.parse(token);

        //Date expotyTime= signedJWT.getJWTClaimsSet().getExpirationTime();

        // Kiểm tra chữ ký của token có hợp lệ không.
        // Lấy chữ ký trong token (phần sau cùng, sau dấu .).
        //Dùng SIGNER_KEY để tính lại chữ ký từ header + payload
        var verified = signedJWT.verify(verifier);
        // expotyTime.after((new Date()))
        return  IntrospectReponse.builder()
                .valid( verified   ) // tokencon hay het han
                .build();

    }
    public AuthenticationReponse authenticate (AuthenticationResquest authenticationResquest) {
        var user = userRepository.findByUsername(authenticationResquest.getUsername())
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationResquest.getPassword(), user.getPassword());
        if (!authenticated)
            throw  new AppException(ErorrCode.UNAUTHENTICATED);

        var token = generateToken(authenticationResquest.getUsername());

        return AuthenticationReponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private String generateToken(String username)  {
        // 1) tap header voi thuat toan ky HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // 2) PayLoad (JWT Claims ) : nội dung mang thông tin người dùng + thời hạn
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username) // định danh chủ thể
                .issuer("devteria.com") // bên phát hành token
                .issueTime(new Date()) // thời điểm phát hành
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // thời điểm hết hạn
                .claim("customClaim","Custom") // tùy chọn
                .build();

        // 3) đóng gói payload vào JWSObject
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        // 4) ký token bằng HMAC với khóa bí mật
        try {
            // Dùng để ký (sign) JWT bằng thuật toán HMAC
            // Đây là bước ký (sign) JWT.
            //Nó sẽ lấy:
            //Header (ví dụ: { "alg": "HS512", "typ": "JWT" })
            //Payload (claims: sub, exp, issuer...)
            //→ ghép lại thành chuỗi "header.payload".
            //Sau đó dùng thuật toán HMAC-SHA512 + SIGNER_KEY để tạo chữ ký số (signature).
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); // trả về chuỗi JWT
        } catch (JOSEException e){
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }

    }
}
