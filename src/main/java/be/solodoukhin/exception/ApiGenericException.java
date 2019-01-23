package be.solodoukhin.exception;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
public class ApiGenericException extends Exception {

    public ApiGenericException(String message) {
        super(message);
    }

    public ApiGenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
