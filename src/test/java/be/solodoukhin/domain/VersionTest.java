package be.solodoukhin.domain;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.Version;
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
public class VersionTest extends ApplicationWithoutContextTest {

    private static final String VERSION_TEST_NAME = "UNIT_TEST";

    @Test
    public void testConstructor() {
        log.info("VersionTest.testConstructor()");
        Version version = new Version();
        Assert.assertNotNull(version);
    }

    @Test
    public void testFields() {
        log.info("VersionTest.testFields()");
        Version version = new Version();
        version.setName(VERSION_TEST_NAME);
        version.setDfaName("0");
        version.setDescription("0");
        version.setStructure(new Structure());
        version.setSignature(new PersistenceSignature());

        Assert.assertNotNull(version);
        Assert.assertEquals(VERSION_TEST_NAME, version.getName());
        Assert.assertTrue(version.getDfaName().isPresent());
        Assert.assertEquals("0", version.getDfaName().get());
        Assert.assertEquals("0", version.getDescription());
        Assert.assertNotNull(version.getStructure());
        Assert.assertNotNull(version.getStructure());
    }
}
