package kz.oneoiq.identity.application;

import kz.oneoiq.identity.domain.exception.InvalidCredentialsException;
import kz.oneoiq.identity.domain.model.RefreshToken;
import kz.oneoiq.identity.domain.model.TokenPair;
import kz.oneoiq.identity.domain.port.in.RefreshTokenUseCase;
import kz.oneoiq.identity.domain.port.out.RefreshTokenRepository;
import kz.oneoiq.identity.domain.port.out.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private static final long ACCESS_TOKEN_TTL_SEC = 900L;
    private static final int REFRESH_TOKEN_TTL_DAYS = 30;

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Override
    @Transactional
    public TokenPair refresh(Command command) {
        RefreshToken existing = refreshTokenRepository.findByTokenHash(command.refreshToken())
                .orElseThrow(InvalidCredentialsException::new);

        if (!existing.isValid()) {
            throw new InvalidCredentialsException();
        }

        // Rotate: revoke old, issue new
        existing.revoke();
        refreshTokenRepository.save(existing);

        String rawRefresh = tokenService.generateOpaqueRefreshToken();
        RefreshToken next = new RefreshToken();
        next.setId(UUID.randomUUID());
        next.setUserId(existing.getUserId());
        next.setTokenHash(rawRefresh);
        next.setDeviceId(command.deviceId());
        next.setExpiresAt(OffsetDateTime.now().plusDays(REFRESH_TOKEN_TTL_DAYS));
        refreshTokenRepository.save(next);

        return new TokenPair(
                tokenService.generateAccessToken(existing.getUserId()),
                rawRefresh,
                ACCESS_TOKEN_TTL_SEC
        );
    }
}
