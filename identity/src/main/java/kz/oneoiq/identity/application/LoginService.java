package kz.oneoiq.identity.application;

import kz.oneoiq.identity.domain.exception.EmailNotVerifiedException;
import kz.oneoiq.identity.domain.exception.InvalidCredentialsException;
import kz.oneoiq.identity.domain.model.RefreshToken;
import kz.oneoiq.identity.domain.model.TokenPair;
import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.in.LoginUseCase;
import kz.oneoiq.identity.domain.port.out.PasswordHasher;
import kz.oneoiq.identity.domain.port.out.RefreshTokenRepository;
import kz.oneoiq.identity.domain.port.out.TokenService;
import kz.oneoiq.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private static final long ACCESS_TOKEN_TTL_SEC = 900L;
    private static final int REFRESH_TOKEN_TTL_DAYS = 30;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    @Override
    @Transactional
    public TokenPair login(Command command) {
        User user = userRepository
                .findByEmailOrUsernameIgnoreCase(command.emailOrUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        String rawRefresh = tokenService.generateOpaqueRefreshToken();
        RefreshToken token = new RefreshToken();
        token.setId(UUID.randomUUID());
        token.setUserId(user.getId());
        token.setTokenHash(rawRefresh);
        token.setDeviceId(command.deviceId());
        token.setExpiresAt(OffsetDateTime.now().plusDays(REFRESH_TOKEN_TTL_DAYS));
        refreshTokenRepository.save(token);

        return new TokenPair(
                tokenService.generateAccessToken(user.getId()),
                rawRefresh,
                ACCESS_TOKEN_TTL_SEC
        );
    }
}
