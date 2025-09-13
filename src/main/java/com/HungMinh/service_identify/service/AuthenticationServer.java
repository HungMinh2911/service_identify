package com.HungMinh.service_identify.service;

import com.HungMinh.service_identify.dto.request.AuthenticationResquest;
import com.HungMinh.service_identify.dto.request.IntrospectResquest;
import com.HungMinh.service_identify.dto.response.AuthenticationReponse;
import com.HungMinh.service_identify.dto.response.IntrospectReponse;
import com.HungMinh.service_identify.entity.User;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;

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

        Date expotyTime= signedJWT.getJWTClaimsSet().getExpirationTime();

        // Kiểm tra chữ ký của token có hợp lệ không.
        // Lấy chữ ký trong token (phần sau cùng, sau dấu .).
        //Dùng SIGNER_KEY để tính lại chữ ký từ header + payload
        var verified = signedJWT.verify(verifier);

        return  IntrospectReponse.builder()
                .valid(verified &&  expotyTime.after((new Date()))) // tokencon hay het han
                .build();

    }
    public AuthenticationReponse authenticate (AuthenticationResquest authenticationResquest) throws JOSEException {

        var user = userRepository.findByUsername(authenticationResquest.getUsername())
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationResquest.getPassword(), user.getPassword());
        if (!authenticated)
            throw  new AppException(ErorrCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationReponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private String generateToken(User user) throws JOSEException {
        // 1) tao header voi thuat toan HS512
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        // 2) noi dung mang thong tin nguoi dung + thoi han
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // dinh danh chu the
                .issuer("HungMinh.com") // ben phan hanh token
                .issueTime(new Date()) // thoi diem phat hanh
                .expirationTime(new Date(
                        Instant.now().plus(1,ChronoUnit.HOURS).toEpochMilli()
                )) // thoi diem het han
                .claim("scope",builScope(user))
                .build();
        // toJSONObject() là cầu nối từ thế giới Java (Map) sang thế giới JSON (chuẩn Internet).
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        // ky tao chu ky so cho token
       jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

       return jwsObject.serialize();

    }
    private String builScope (User user){
        // tao 1 chuoi cach nhau bang dau cach
        StringJoiner stringJoiner = new StringJoiner(" ");
        //if (!CollectionUtils.isEmpty(user.getRoles()))
            // Dùng :: để tham chiếu trực tiếp tới method add của stringJoiner.
            //user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();

    }
}
