package kz.oneoiq.identity.application;

import kz.oneoiq.common.event.identity.UserRegisteredEvent;
import kz.oneoiq.identity.domain.exception.UserNotFoundException;
import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;
import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.in.ResendVerificationUseCase;
import kz.oneoiq.identity.domain.port.out.CodeGenerator;
import kz.oneoiq.identity.domain.port.out.DomainEventPublisher;
import kz.oneoiq.identity.domain.port.out.EmailVerificationRepository;
import kz.oneoiq.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResendVerificationService implements ResendVerificationUseCase {

    private static final String TOPIC_USER_REGISTERED = "identity.user.registered";
    private static final int CODE_TTL_MINUTES = 15;

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final DomainEventPublisher eventPublisher;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional
    public void resend(Command command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        String rawCode = codeGenerator.generate();
        EmailVerification verification = new EmailVerification();
        verification.setId(UUID.randomUUID());
        verification.setUserId(user.getId());
        verification.setCodeHash(codeGenerator.hash(rawCode));
        verification.setPurpose(EmailVerificationPurpose.REGISTRATION);
        verification.setExpiresAt(OffsetDateTime.now().plusMinutes(CODE_TTL_MINUTES));
        verification.setAttempts(0);
        emailVerificationRepository.save(verification);

        // Reuse UserRegisteredEvent — mail service only uses email, username, verificationCode
        eventPublisher.publish(TOPIC_USER_REGISTERED, new UserRegisteredEvent(
                UUID.randomUUID(),
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                rawCode
        ));
    }
}
