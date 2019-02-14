package be.solodoukhin.service.security;

import be.solodoukhin.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.02.13
 */
public class AuthenticationServiceTest extends ApplicationTest {

    @Autowired
    private AuthenticationService service;

    @Test
    public void testAuthenticate() {
        final String token = "JB6OS7F1EE2TSFRQMDN32PEIC0RF2YOEYG4GTEGUIZTA2PF6YU3808GVBY4V54FCU7AMT7K5CHE5FUPA00";
        Assert.assertTrue(service.getCurrentUserName(token, LocalDate.of(2015, 5, 15)).isPresent());
    }

    @Test
    public void testAuthenticationFailed() {
        final String token = "99999999Z99999999999999999%9999999999999999*999999999999/999999*99-";
        Assert.assertFalse(service.getCurrentUserName(token, LocalDate.now()).isPresent());
    }

    @Test
    public void testAuthorization() {
        final String username = "TEST_CEN";
        final LocalDate validUntil = LocalDate.now();
        final String application = "RESET_PASSWORD";

        Assert.assertTrue(service.hasAccess(username, validUntil, application));
    }

    @Test
    public void testAuthorizationFailedBadUsername() {
        final String username = "TEST_MACHIN-99*01";
        final LocalDate validUntil = LocalDate.now();
        final String application = "RESET_PASSWORD";

        Assert.assertFalse(service.hasAccess(username, validUntil, application));
    }

    @Test
    public void testAuthorizationFailedBadApplication() {
        final String username = "TEST_CEN";
        final LocalDate validUntil = LocalDate.now();
        final String application = "UNKNOWN_APP-98*-1";

        Assert.assertFalse(service.hasAccess(username, validUntil, application));
    }
}
