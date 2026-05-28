package kz.oneoiq.identity.domain.port.out;

import java.util.UUID;

public interface TokenService {

    String generateAccessToken(UUID userId);

    String generateOpaqueRefreshToken();
}
