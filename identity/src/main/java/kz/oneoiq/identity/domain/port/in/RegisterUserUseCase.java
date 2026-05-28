package kz.oneoiq.identity.domain.port.in;

import java.util.UUID;

public interface RegisterUserUseCase {

    Result register(Command command);

    record Command(String email, String username, String password, String displayName) {}

    record Result(UUID userId) {}
}
