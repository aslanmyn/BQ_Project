package kz.oneoiq.identity.domain.exception;

public class EmailTakenException extends IdentityException {
    public EmailTakenException() {
        super("Email already taken");
    }
}
