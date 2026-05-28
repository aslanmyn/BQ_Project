package kz.oneoiq.identity.bootstrap;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kz.oneoiq.identity.bootstrap.config.JwtProperties;
import kz.oneoiq.identity.domain.port.out.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateAccessToken(UUID userId) {
        SecretKey key = signingKey();
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtProperties.accessTokenTtlSec() * 1000))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateOpaqueRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
