package be.solodoukhin.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.02.14
 */
public class AccessNotAllowedException extends AccountStatusException {
    public AccessNotAllowedException(String msg) {
        super(msg);
    }

    public AccessNotAllowedException(String msg, Throwable t) {
        super(msg, t);
    }
}
