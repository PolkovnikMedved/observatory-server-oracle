package be.solodoukhin;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public abstract class ApplicationWithoutContextTest {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
}
