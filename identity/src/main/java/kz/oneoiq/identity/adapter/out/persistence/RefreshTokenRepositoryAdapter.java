package kz.oneoiq.identity.adapter.out.persistence;

import kz.oneoiq.identity.domain.model.RefreshToken;
import kz.oneoiq.identity.domain.port.out.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpa;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpa.save(token);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpa.findByTokenHash(tokenHash);
    }

    @Override
    public void revokeAllByUserId(java.util.UUID userId) {
        jpa.revokeAllByUserId(userId, OffsetDateTime.now());
    }
}
