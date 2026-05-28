package kz.oneoiq.identity.adapter.in.web;

import kz.oneoiq.identity.domain.port.in.ResolveUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final ResolveUserUseCase resolveUserUseCase;

    @GetMapping("/resolve")
    public ResolveResponse resolve(@RequestParam String emailOrUsername) {
        ResolveUserUseCase.Result result = resolveUserUseCase.resolve(
                new ResolveUserUseCase.Command(emailOrUsername)
        );
        return new ResolveResponse(result.userId(), result.email(), result.username());
    }

    public record ResolveResponse(UUID userId, String email, String username) {}
}
