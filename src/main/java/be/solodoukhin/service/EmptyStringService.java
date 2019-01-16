package be.solodoukhin.service;

import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.16
 */
@Service
public class EmptyStringService {

    /**
     * When we receive URL parameters in a GET method they
     * are sometimes set as null and sometimes as empty strings.
     *
     * We need to parse this input parameters and return null if
     * the input is null or empty.
     */
    String parseEmptyString(String input) {
        if(input == null || input.trim().isEmpty()) {
            return null;
        }
        return input;
    }
}
