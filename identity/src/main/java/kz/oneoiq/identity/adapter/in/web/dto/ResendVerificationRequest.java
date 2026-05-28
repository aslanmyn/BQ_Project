package kz.oneoiq.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ResendVerificationRequest(
        @NotNull UUID userId
) {}
