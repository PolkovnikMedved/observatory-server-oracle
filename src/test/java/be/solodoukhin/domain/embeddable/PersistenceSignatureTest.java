package be.solodoukhin.domain.embeddable;

import be.solodoukhin.ApplicationWithoutContextTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class PersistenceSignatureTest extends ApplicationWithoutContextTest {

    private static final String AUTHOR = "SOLODOUV";
    private static final LocalDate DATE = LocalDate.now();

    @Test
    public void testConstructor() {
        LOGGER.info("PersistenceSignatureTest.testConstructor()");
        PersistenceSignature signature = new PersistenceSignature();
        Assert.assertNotNull(signature);

        signature = new PersistenceSignature("BROL");
        Assert.assertNotNull(signature);
    }

    @Test
    public void testFields() {
        LOGGER.info("PersistenceSignatureTest.testFields()");
        PersistenceSignature signature = new PersistenceSignature();
        signature.setCreatedBy(AUTHOR);
        signature.setCreatedAt(DATE);
        signature.setModifiedBy(AUTHOR);
        signature.setModifiedAt(DATE);

        Assert.assertNotNull(signature);
        Assert.assertEquals(AUTHOR, signature.getCreatedBy());
        Assert.assertEquals(DATE, signature.getCreatedAt());
        Assert.assertEquals(AUTHOR, signature.getModifiedBy());
        Assert.assertEquals(DATE, signature.getModifiedAt());
    }

    @Test
    public void testModification() {
        LOGGER.info("PersistenceSignatureTest.testModification()");
        PersistenceSignature signature = new PersistenceSignature(AUTHOR);
        Assert.assertNotNull(signature);
        signature.setModification(AUTHOR);
        Assert.assertEquals(AUTHOR, signature.getModifiedBy());
    }
}
