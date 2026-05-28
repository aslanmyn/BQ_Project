package kz.oneoiq.identity.adapter.out.persistence;

import kz.oneoiq.identity.domain.model.EmailVerification;
import kz.oneoiq.identity.domain.model.EmailVerificationPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    @Query("""
            SELECT v FROM EmailVerification v
            WHERE v.userId = :userId AND v.purpose = :purpose
            ORDER BY v.expiresAt DESC
            LIMIT 1
            """)
    Optional<EmailVerification> findLatestByUserIdAndPurpose(
            @Param("userId") UUID userId,
            @Param("purpose") EmailVerificationPurpose purpose
    );
}
