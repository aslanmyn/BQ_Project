package kz.oneoiq.identity.adapter.in.web.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresInSec
) {}
