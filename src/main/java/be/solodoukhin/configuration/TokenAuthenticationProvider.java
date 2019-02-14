package be.solodoukhin.configuration;

import be.solodoukhin.domain.User;
import be.solodoukhin.exception.AccessNotAllowedException;
import be.solodoukhin.service.security.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.02.13
 */
@Slf4j
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private AuthenticationService authenticationService;

    @Autowired
    public TokenAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        if(!authenticationService.hasAccess(userDetails.getUsername(), LocalDate.now())) {
            log.warn("The user doesn't have access to this application");
            throw new AccessNotAllowedException("The user with username = " + userDetails.getUsername() + " doesn't have access to this resource");
        }
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        String token = (String) usernamePasswordAuthenticationToken.getCredentials();

        Optional<String> username = authenticationService.getCurrentUserName(token, LocalDate.now());
        if(!username.isPresent()) {
            String tokenSubstring = (token.length() > 20 ? token.substring(0, 20) : token);
            log.warn("Could not find user with token starting with = '{}'", tokenSubstring);
            throw new UsernameNotFoundException("Could not find user with token starting with = " + tokenSubstring);
        }

        return new User(username.get());
    }
}
