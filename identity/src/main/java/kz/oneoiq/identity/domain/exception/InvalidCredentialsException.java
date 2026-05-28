package kz.oneoiq.identity.domain.exception;

public class InvalidCredentialsException extends IdentityException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
