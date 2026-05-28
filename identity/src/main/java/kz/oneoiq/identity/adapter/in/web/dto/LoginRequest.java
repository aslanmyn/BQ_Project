package kz.oneoiq.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String emailOrUsername,
        @NotBlank String password,
        String deviceId
) {}
