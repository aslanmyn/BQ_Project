package kz.oneoiq.identity.domain.exception;

public class UsernameTakenException extends IdentityException {
    public UsernameTakenException() {
        super("Username already taken");
    }
}
