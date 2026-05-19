package com.web.registration.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.web.registration.config.JwtProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private byte[] secretBytes;

    @PostConstruct
    public void init() {
        this.secretBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
    }

    public String generateAccessToken(String username, String role) {
        try {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("roles", List.of(role))
                    .issueTime(now)
                    .expirationTime(expiry)
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(new MACSigner(secretBytes));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Không thể tạo JWT token", e);
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(secretBytes);
            return signedJWT.verify(verifier)
                    && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }
}
