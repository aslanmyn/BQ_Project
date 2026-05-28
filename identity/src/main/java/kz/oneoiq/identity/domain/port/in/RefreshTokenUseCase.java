package kz.oneoiq.identity.domain.port.in;

import kz.oneoiq.identity.domain.model.TokenPair;

public interface RefreshTokenUseCase {

    TokenPair refresh(Command command);

    record Command(String refreshToken, String deviceId) {}
}
