package kz.oneoiq.identity.adapter.out.persistence;

import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;
import kz.oneoiq.identity.domain.port.out.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailVerificationRepositoryAdapter implements EmailVerificationRepository {

    private final JpaEmailVerificationRepository jpa;

    @Override
    public EmailVerification save(EmailVerification verification) {
        return jpa.save(verification);
    }

    @Override
    public Optional<EmailVerification> findLatestByUserIdAndPurpose(UUID userId, EmailVerificationPurpose purpose) {
        return jpa.findLatestByUserIdAndPurpose(userId, purpose);
    }
}
