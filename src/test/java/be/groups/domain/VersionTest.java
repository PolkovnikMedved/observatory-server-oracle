package be.groups.domain;

import be.groups.AbstractConfiguredTest;
import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class VersionTest extends AbstractConfiguredTest {

    private static final String VERSION_TEST_NAME = "UNIT_TEST";

    @Test
    public void testConstructor() {
        LOGGER.info("VersionTest.testConstructor()");
        Version version = new Version();
        Assert.assertNotNull(version);
    }

    @Test
    public void testFields() {
        LOGGER.info("VersionTest.testFields()");
        Version version = new Version();
        version.setName(VERSION_TEST_NAME);
        version.setDfaName("0");
        version.setDescription("0");
        version.setStructure(new Structure());
        version.setSignature(new PersistenceSignature());

        Assert.assertNotNull(version);
        Assert.assertEquals(VERSION_TEST_NAME, version.getName());
        Assert.assertEquals("0", version.getDfaName());
        Assert.assertEquals("0", version.getDescription());
        Assert.assertNotNull(version.getStructure());
        Assert.assertNotNull(version.getStructure());
    }
}
