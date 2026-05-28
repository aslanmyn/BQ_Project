package kz.oneoiq.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VerifyEmailRequest(
        @NotNull UUID userId,
        @NotBlank String code
) {}
