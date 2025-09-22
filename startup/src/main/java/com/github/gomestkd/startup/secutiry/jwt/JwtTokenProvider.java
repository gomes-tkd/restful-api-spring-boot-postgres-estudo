package com.github.gomestkd.startup.secutiry.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.gomestkd.startup.data.dto.security.TokenDTO;
import com.github.gomestkd.startup.exception.InvalidJWTAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${security.jwt.token.secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    private Algorithm algorithm;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
        logger.info("JWT Provider initialized with HMAC256 algorithm.");
    }

    public TokenDTO createAccessToken(String username, Set<String> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusSeconds(validityInMilliseconds);

        String accessToken = buildAccessToken(username, roles, now, validity);
        String refreshToken = buildRefreshToken(username, roles, now);

        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        String token = stripBearer(refreshToken);

        JWTVerifier verifier = JWT.require(algorithm).build();

        if (token == null) {
            throw new RuntimeException("Refresh token is missing or invalid");
        }

        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        Set<String> roles = new HashSet<>(decodedJWT.getClaim("roles").asList(String.class));

        return createAccessToken(username, roles);

    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return stripBearer(bearerToken);
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);
            Instant expiration = decodedJWT.getExpiresAt().toInstant();

            return expiration.isAfter(Instant.now());
        } catch (Exception e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            throw new InvalidJWTAuthenticationException("Expired or Invalid JWT Token!");
        }
    }

    private String buildRefreshToken(String username, Set<String> roles, Instant now) {
        Instant refreshTokenValidity = now.plusSeconds(validityInMilliseconds * 3);

        return JWT.create()
                .withClaim("roles", roles.stream().toList())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(refreshTokenValidity))
                .withSubject(username)
                .sign(algorithm);
    }

    private String buildAccessToken(String username, Set<String> roles, Instant now, Instant validity) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles.stream().toList())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(validity))
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm);
    }

    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private String stripBearer(String bearerToken) {
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
