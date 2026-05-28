package kz.oneoiq.identity.domain.port.in;

import java.util.UUID;

public interface ResendVerificationUseCase {

    void resend(Command command);

    record Command(UUID userId) {}
}
