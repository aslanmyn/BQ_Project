package kz.oneoiq.identity.domain.exception;

public class EmailNotVerifiedException extends IdentityException {
    public EmailNotVerifiedException() {
        super("Email not verified");
    }
}
