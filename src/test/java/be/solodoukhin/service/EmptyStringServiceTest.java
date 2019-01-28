package be.solodoukhin.service;

import be.solodoukhin.ApplicationWithoutContextTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.28
 */
@Slf4j
public class EmptyStringServiceTest extends ApplicationWithoutContextTest {

    private EmptyStringService emptyStringService = new EmptyStringService();

    @Test
    public void testParseEmptyString() {
        log.info("Call to EmptyStringServiceTest.testParseEmptyString()");
        String empty = "";
        String moreThanEmpty = null;
        Assert.assertNull(emptyStringService.parseEmptyString(empty));
        Assert.assertNull(emptyStringService.parseEmptyString(moreThanEmpty));
    }
}
