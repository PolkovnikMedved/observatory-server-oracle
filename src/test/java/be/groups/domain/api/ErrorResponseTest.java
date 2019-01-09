package be.groups.domain.api;

import be.groups.AbstractConfiguredTest;
import be.solodoukhin.domain.api.ErrorResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class ErrorResponseTest extends AbstractConfiguredTest {

    @Test
    public void testConstructor() {
        LOGGER.info("ErrorResponseTest.testConstructor()");
        ErrorResponse response = new ErrorResponse(400, "Aretez de taper n'importe quoi dans notre application géniale !");
        Assert.assertNotNull(response);
    }

    @Test
    public void testFields() {
        LOGGER.info("ErrorResponseTest.testConstructor()");
        ErrorResponse response = new ErrorResponse(200, "Welcome brother.");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
        Assert.assertEquals("Welcome brother.", response.getMessage());
    }
}
