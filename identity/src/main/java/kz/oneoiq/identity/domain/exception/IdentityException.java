package kz.oneoiq.identity.domain.exception;

public abstract class IdentityException extends RuntimeException {
    protected IdentityException(String message) {
        super(message);
    }
}
