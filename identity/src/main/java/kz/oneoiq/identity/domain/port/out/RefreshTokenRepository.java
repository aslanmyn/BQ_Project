package kz.oneoiq.identity.domain.port.out;

import kz.oneoiq.identity.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void revokeAllByUserId(java.util.UUID userId);
}
