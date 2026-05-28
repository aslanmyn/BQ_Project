package kz.oneoiq.identity.domain.port.in;

import kz.oneoiq.identity.domain.model.TokenPair;

public interface LoginUseCase {

    TokenPair login(Command command);

    record Command(String emailOrUsername, String password) {}
}
