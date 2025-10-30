package ru.ivanov.ecommerceplatformproject.cartservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWTUtils {

    @Value("${jwt.access.secret}")
    String accessTokenSecret;

    public boolean isAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey(accessTokenSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("type").equals("access");
        } catch (ExpiredJwtException e) {
            // Просроченный токен все еще считается access-токеном
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims validateAndParseAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey(accessTokenSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Дополнительная проверка обязательных claims
            if (claims.getSubject() == null ||
                claims.get("roles") == null) {
                throw new MalformedJwtException("Missing required claims in access token");
            }

            return claims;
        } catch (ExpiredJwtException e) {
            throw new JwtException("Access token expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT format", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Malformed JWT", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid signature", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid token", e);
        }
    }

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}