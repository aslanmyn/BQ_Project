package kz.oneoiq.identity.domain.exception;

public class InvalidCodeException extends IdentityException {
    public InvalidCodeException() {
        super("Invalid verification code");
    }
}
