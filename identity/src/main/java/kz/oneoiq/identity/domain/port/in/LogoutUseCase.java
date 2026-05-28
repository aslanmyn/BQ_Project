package kz.oneoiq.identity.domain.port.in;

public interface LogoutUseCase {

    void logout(Command command);

    record Command(String refreshToken) {}
}
