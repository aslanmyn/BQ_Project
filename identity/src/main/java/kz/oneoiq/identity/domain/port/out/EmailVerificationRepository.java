package kz.oneoiq.identity.domain.port.out;

import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository {

    EmailVerification save(EmailVerification verification);

    Optional<EmailVerification> findLatestByUserIdAndPurpose(UUID userId, EmailVerificationPurpose purpose);
}
