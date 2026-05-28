package kz.oneoiq.common.event.identity;

import java.util.UUID;

public record UserEmailVerifiedEvent(
        UUID eventId,
        UUID userId,
        String email
) {}
