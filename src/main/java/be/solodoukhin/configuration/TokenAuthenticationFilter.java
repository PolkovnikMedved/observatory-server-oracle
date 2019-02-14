package be.solodoukhin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.02.13
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${token.pattern}")
    private String tokenPattern;

    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        final String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if(authorization == null) {
            throw new BadCredentialsException("Missing '" + AUTHORIZATION_HEADER +"' header." );
        }

        final Pattern pattern = Pattern.compile(tokenPattern);
        final Matcher matcher = pattern.matcher(authorization);

        if(matcher.find() && matcher.groupCount() == 1) {
            final Authentication auth = new UsernamePasswordAuthenticationToken(matcher.group(1), matcher.group(1));
            return getAuthenticationManager().authenticate(auth);
        } else {
            throw new BadCredentialsException("Missing token");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
