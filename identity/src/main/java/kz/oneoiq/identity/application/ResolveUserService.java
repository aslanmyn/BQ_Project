package kz.oneoiq.identity.application;

import kz.oneoiq.identity.domain.exception.UserNotFoundException;
import kz.oneoiq.identity.domain.model.User;
import kz.oneoiq.identity.domain.port.in.ResolveUserUseCase;
import kz.oneoiq.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResolveUserService implements ResolveUserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Result resolve(Command command) {
        User user = userRepository
                .findByEmailOrUsernameIgnoreCase(command.emailOrUsername())
                .orElseThrow(() -> new UserNotFoundException(command.emailOrUsername()));

        return new Result(user.getId(), user.getEmail(), user.getUsername());
    }
}
