package co.com.foodcourt.model.user.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
