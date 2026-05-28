package kz.oneoiq.common.event.identity;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        UUID userId,
        String email,
        String username,
        String verificationCode
) {}
