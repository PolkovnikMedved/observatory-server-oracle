package be.solodoukhin.domain.api;

import be.solodoukhin.ApplicationWithoutContextTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
@Slf4j
public class ErrorResponseTest extends ApplicationWithoutContextTest {

    @Test
    public void testConstructor() {
        log.info("ErrorResponseTest.testConstructor()");
        ErrorResponse response = new ErrorResponse(400, "Aretez de taper n'importe quoi dans notre application géniale !");
        Assert.assertNotNull(response);
    }

    @Test
    public void testFields() {
        log.info("ErrorResponseTest.testConstructor()");
        ErrorResponse response = new ErrorResponse(200, "Welcome brother.");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
        Assert.assertEquals("Welcome brother.", response.getMessage());
    }
}
