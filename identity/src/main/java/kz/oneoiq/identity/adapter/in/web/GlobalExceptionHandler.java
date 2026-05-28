package kz.oneoiq.identity.adapter.in.web;

import kz.oneoiq.identity.domain.exception.CodeExpiredException;
import kz.oneoiq.identity.domain.exception.EmailNotVerifiedException;
import kz.oneoiq.identity.domain.exception.EmailTakenException;
import kz.oneoiq.identity.domain.exception.InvalidCodeException;
import kz.oneoiq.identity.domain.exception.InvalidCredentialsException;
import kz.oneoiq.identity.domain.exception.UserNotFoundException;
import kz.oneoiq.identity.domain.exception.UsernameTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailTakenException.class)
    public ProblemDetail handleEmailTaken(EmailTakenException ex) {
        return problem(HttpStatus.CONFLICT, "EMAIL_TAKEN", ex.getMessage());
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ProblemDetail handleUsernameTaken(UsernameTakenException ex) {
        return problem(HttpStatus.CONFLICT, "USERNAME_TAKEN", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {
        return problem(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ProblemDetail handleEmailNotVerified(EmailNotVerifiedException ex) {
        return problem(HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED", ex.getMessage());
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ProblemDetail handleInvalidCode(InvalidCodeException ex) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_CODE", ex.getMessage());
    }

    @ExceptionHandler(CodeExpiredException.class)
    public ProblemDetail handleCodeExpired(CodeExpiredException ex) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "CODE_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problem(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", detail);
    }

    private ProblemDetail problem(HttpStatus status, String errorCode, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setProperty("errorCode", errorCode);
        return pd;
    }
}
