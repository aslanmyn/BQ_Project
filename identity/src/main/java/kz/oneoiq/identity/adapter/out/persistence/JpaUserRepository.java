package kz.oneoiq.identity.adapter.out.persistence;

import kz.oneoiq.identity.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:value) OR LOWER(u.username) = LOWER(:value)")
    Optional<User> findByEmailOrUsernameIgnoreCase(@Param("value") String value);
}
