package kz.oneoiq.identity.domain.exception;

public class UserNotFoundException extends IdentityException {
    public UserNotFoundException() {
        super("User not found");
    }
}
