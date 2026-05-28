package kz.oneoiq.identity.adapter.out.persistence;

import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpa;

    @Override public User save(User user) { return jpa.save(user); }
    @Override public Optional<User> findById(UUID id) { return jpa.findById(id); }
    @Override public Optional<User> findByEmailIgnoreCase(String email) { return jpa.findByEmailIgnoreCase(email); }
    @Override public Optional<User> findByUsernameIgnoreCase(String username) { return jpa.findByUsernameIgnoreCase(username); }
    @Override public Optional<User> findByEmailOrUsernameIgnoreCase(String value) { return jpa.findByEmailOrUsernameIgnoreCase(value); }
    @Override public boolean existsByEmailIgnoreCase(String email) { return jpa.existsByEmailIgnoreCase(email); }
    @Override public boolean existsByUsernameIgnoreCase(String username) { return jpa.existsByUsernameIgnoreCase(username); }
}
