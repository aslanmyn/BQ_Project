package kz.oneoiq.identity.domain.port.in;

import java.util.UUID;

public interface ResolveUserUseCase {

    Result resolve(Command command);

    record Command(String emailOrUsername) {}

    record Result(UUID userId, String email, String username) {}
}
