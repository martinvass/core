package com.github.martinyes.account.link;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class JwtService {

    private static final String SECRET = "";
    private static final String ENDPOINT = "https://127.0.01/link?token=%s";

    private static final Cache<String, String> TOKEN_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public static String getOrCreateToken(String username) {
        try {
            if (TOKEN_CACHE.getIfPresent(username) != null)
                return TOKEN_CACHE.getIfPresent(username);

            Algorithm algorithm = Algorithm.HMAC512(SECRET);
            LocalDateTime time = LocalDateTime.now();

            return String.format(ENDPOINT, JWT.create()
                    .withClaim("userName", username)
                    .withExpiresAt(java.sql.Timestamp.valueOf(time.plusMinutes(10)))
                    .sign(algorithm));
        } catch (JWTCreationException e) {
            e.printStackTrace();
            return null;
        }
    }
}