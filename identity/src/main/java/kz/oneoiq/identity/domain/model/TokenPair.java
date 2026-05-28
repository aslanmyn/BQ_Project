package kz.oneoiq.identity.domain.model;

public record TokenPair(String accessToken, String refreshToken, long expiresInSec) {}
