package com.github.gomestkd.startup.data.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class TokenDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private Boolean authenticated;
    private Instant createdAt;
    private Instant expiresAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String accessToken;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String refreshToken;

    public TokenDTO() {
    }

    public TokenDTO(String username, Boolean authenticated, Instant createdAt, Instant expiresAt, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TokenDTO tokenDTO)) return false;
        return Objects.equals(getUsername(), tokenDTO.getUsername()) && Objects.equals(getAuthenticated(), tokenDTO.getAuthenticated()) && Objects.equals(getCreatedAt(), tokenDTO.getCreatedAt()) && Objects.equals(getExpiresAt(), tokenDTO.getExpiresAt()) && Objects.equals(getAccessToken(), tokenDTO.getAccessToken()) && Objects.equals(getRefreshToken(), tokenDTO.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthenticated(), getCreatedAt(), getExpiresAt(), getAccessToken(), getRefreshToken());
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "username='" + username + '\'' +
                ", authenticated=" + authenticated +
                ", created=" + createdAt +
                ", expiration=" + expiresAt +
                ", accessToken=****" +
                ", refreshToken=****" +
                '}';
    }
}
