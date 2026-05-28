package kz.oneoiq.identity.adapter.in.web;

import jakarta.validation.Valid;
import kz.oneoiq.identity.adapter.in.web.dto.LoginRequest;
import kz.oneoiq.identity.adapter.in.web.dto.LogoutRequest;
import kz.oneoiq.identity.adapter.in.web.dto.RefreshRequest;
import kz.oneoiq.identity.adapter.in.web.dto.RegisterRequest;
import kz.oneoiq.identity.adapter.in.web.dto.RegisterResponse;
import kz.oneoiq.identity.adapter.in.web.dto.ResendVerificationRequest;
import kz.oneoiq.identity.adapter.in.web.dto.TokenResponse;
import kz.oneoiq.identity.adapter.in.web.dto.VerifyEmailRequest;
import kz.oneoiq.identity.domain.model.TokenPair;
import kz.oneoiq.identity.domain.port.in.LoginUseCase;
import kz.oneoiq.identity.domain.port.in.LogoutUseCase;
import kz.oneoiq.identity.domain.port.in.RefreshTokenUseCase;
import kz.oneoiq.identity.domain.port.in.RegisterUserUseCase;
import kz.oneoiq.identity.domain.port.in.ResendVerificationUseCase;
import kz.oneoiq.identity.domain.port.in.VerifyEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ResendVerificationUseCase resendVerificationUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserUseCase.Result result = registerUserUseCase.register(
                new RegisterUserUseCase.Command(
                        request.email(),
                        request.username(),
                        request.password(),
                        request.displayName()
                )
        );
        return new RegisterResponse(result.userId());
    }

    @PostMapping("/verify-email")
    public TokenResponse verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        TokenPair pair = verifyEmailUseCase.verify(
                new VerifyEmailUseCase.Command(request.userId(), request.code())
        );
        return toResponse(pair);
    }

    @PostMapping("/resend-verification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        resendVerificationUseCase.resend(new ResendVerificationUseCase.Command(request.userId()));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        TokenPair pair = loginUseCase.login(
                new LoginUseCase.Command(request.emailOrUsername(), request.password(), request.deviceId())
        );
        return toResponse(pair);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        TokenPair pair = refreshTokenUseCase.refresh(
                new RefreshTokenUseCase.Command(request.refreshToken(), request.deviceId())
        );
        return toResponse(pair);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody LogoutRequest request) {
        logoutUseCase.logout(new LogoutUseCase.Command(request.refreshToken()));
    }

    private TokenResponse toResponse(TokenPair pair) {
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.expiresInSec());
    }
}
