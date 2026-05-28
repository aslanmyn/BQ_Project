package kz.oneoiq.identity.application;

import kz.oneoiq.common.event.identity.UserRegisteredEvent;
import kz.oneoiq.identity.domain.exception.EmailTakenException;
import kz.oneoiq.identity.domain.exception.UsernameTakenException;
import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;
import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.in.RegisterUserUseCase;
import kz.oneoiq.identity.domain.port.out.CodeGenerator;
import kz.oneoiq.identity.domain.port.out.DomainEventPublisher;
import kz.oneoiq.identity.domain.port.out.EmailVerificationRepository;
import kz.oneoiq.identity.domain.port.out.PasswordHasher;
import kz.oneoiq.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private static final String TOPIC_USER_REGISTERED = "identity.user.registered";
    private static final int CODE_TTL_MINUTES = 15;

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final DomainEventPublisher eventPublisher;
    private final PasswordHasher passwordHasher;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional
    public Result register(Command command) {
        if (userRepository.existsByEmailIgnoreCase(command.email())) {
            throw new EmailTakenException(command.email());
        }
        if (userRepository.existsByUsernameIgnoreCase(command.username())) {
            throw new UsernameTakenException(command.username());
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(command.email().toLowerCase());
        user.setUsername(command.username());
        user.setPasswordHash(passwordHasher.hash(command.password()));
        user.setDisplayName(command.displayName() != null ? command.displayName() : command.username());
        user.setEmailVerified(false);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);

        String rawCode = codeGenerator.generate();
        EmailVerification verification = new EmailVerification();
        verification.setId(UUID.randomUUID());
        verification.setUserId(user.getId());
        verification.setCodeHash(codeGenerator.hash(rawCode));
        verification.setPurpose(EmailVerificationPurpose.REGISTRATION);
        verification.setExpiresAt(OffsetDateTime.now().plusMinutes(CODE_TTL_MINUTES));
        verification.setAttempts(0);
        emailVerificationRepository.save(verification);

        eventPublisher.publish(TOPIC_USER_REGISTERED, new UserRegisteredEvent(
                UUID.randomUUID(),
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                rawCode
        ));

        return new Result(user.getId());
    }
}
