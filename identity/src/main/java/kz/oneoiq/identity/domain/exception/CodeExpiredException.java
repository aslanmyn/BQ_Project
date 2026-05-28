package kz.oneoiq.identity.domain.exception;

public class CodeExpiredException extends IdentityException {
    public CodeExpiredException() {
        super("Verification code expired or attempts exceeded");
    }
}
