package kz.oneoiq.identity.application;

import kz.oneoiq.common.event.identity.UserEmailVerifiedEvent;
import kz.oneoiq.identity.domain.exception.CodeExpiredException;
import kz.oneoiq.identity.domain.exception.InvalidCodeException;
import kz.oneoiq.identity.domain.exception.UserNotFoundException;
import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;
import kz.oneoiq.identity.domain.model.RefreshToken;
import kz.oneoiq.identity.domain.model.TokenPair;
import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.in.VerifyEmailUseCase;
import kz.oneoiq.identity.domain.port.out.CodeGenerator;
import kz.oneoiq.identity.domain.port.out.DomainEventPublisher;
import kz.oneoiq.identity.domain.port.out.EmailVerificationRepository;
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
public class VerifyEmailService implements VerifyEmailUseCase {

    private static final String TOPIC_EMAIL_VERIFIED = "identity.user.email-verified";
    private static final long ACCESS_TOKEN_TTL_SEC = 900L; // 15 min
    private static final int REFRESH_TOKEN_TTL_DAYS = 30;

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DomainEventPublisher eventPublisher;
    private final CodeGenerator codeGenerator;
    private final TokenService tokenService;

    @Override
    @Transactional
    public TokenPair verify(Command command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        EmailVerification verification = emailVerificationRepository
                .findLatestByUserIdAndPurpose(command.userId(), EmailVerificationPurpose.REGISTRATION)
                .orElseThrow(InvalidCodeException::new);

        if (verification.isConsumed()) {
            throw new InvalidCodeException();
        }
        if (verification.isExpired()) {
            throw new CodeExpiredException();
        }
        if (verification.isAttemptsExceeded()) {
            throw new InvalidCodeException();
        }

        verification.incrementAttempts();

        if (!codeGenerator.matches(command.code(), verification.getCodeHash())) {
            emailVerificationRepository.save(verification);
            throw new InvalidCodeException();
        }

        verification.consume();
        emailVerificationRepository.save(verification);

        user.verifyEmail();
        userRepository.save(user);

        eventPublisher.publish(TOPIC_EMAIL_VERIFIED, new UserEmailVerifiedEvent(
                UUID.randomUUID(),
                user.getId(),
                user.getEmail()
        ));

        return issueTokenPair(user.getId(), null);
    }

    private TokenPair issueTokenPair(UUID userId, String deviceId) {
        String rawRefresh = tokenService.generateOpaqueRefreshToken();
        RefreshToken token = new RefreshToken();
        token.setId(UUID.randomUUID());
        token.setUserId(userId);
        token.setTokenHash(rawRefresh);
        token.setDeviceId(deviceId);
        token.setExpiresAt(OffsetDateTime.now().plusDays(REFRESH_TOKEN_TTL_DAYS));
        refreshTokenRepository.save(token);

        return new TokenPair(
                tokenService.generateAccessToken(userId),
                rawRefresh,
                ACCESS_TOKEN_TTL_SEC
        );
    }
}
