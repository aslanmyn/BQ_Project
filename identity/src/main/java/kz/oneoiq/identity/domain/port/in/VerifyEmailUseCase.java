package kz.oneoiq.identity.domain.port.in;

import kz.oneoiq.identity.domain.model.TokenPair;

import java.util.UUID;

public interface VerifyEmailUseCase {

    TokenPair verify(Command command);

    record Command(UUID userId, String code) {}
}
