package kz.oneoiq.identity.application;

import kz.oneoiq.identity.domain.model.RefreshToken;
import kz.oneoiq.identity.domain.port.in.LogoutUseCase;
import kz.oneoiq.identity.domain.port.out.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void logout(Command command) {
        refreshTokenRepository.findByTokenHash(command.refreshToken())
                .ifPresent(token -> {
                    token.revoke();
                    refreshTokenRepository.save(token);
                });
    }
}
