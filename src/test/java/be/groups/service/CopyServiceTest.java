package be.groups.service;

import be.groups.AbstractConfiguredTest;
import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.StructureElement;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.service.CopyService;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class CopyServiceTest extends AbstractConfiguredTest {

    private static final String VERSION_TEST_NAME = "UNIT_TEST";
    private static final String TEST_STRUCTURE_NAME = "UNIT_TEST";

    private CopyService service = new CopyService();

    @Test
    public void testDeepVersionCopy()
    {
        LOGGER.info("CopyServiceTest.testDeepVersionCopy()");
        Version version = new Version();
        version.setName(VERSION_TEST_NAME);
        version.setDfaName("0");
        version.setDescription("0");
        version.setStructure(new Structure());
        version.setSignature(new PersistenceSignature());

        Version copy = service.createCopyVersion(version, "COPY");
        Assert.assertNotNull(copy);
        Assert.assertEquals("COPY", copy.getName());
        Assert.assertEquals("0", copy.getDfaName());
        Assert.assertEquals("0", copy.getDescription());
        Assert.assertNotNull(copy.getStructure());
        Assert.assertNull(copy.getSignature());
    }

    @Test
    public void testDeepStructureCopy() {
        LOGGER.info("CopyServiceTest.testDeepStructureCopy()");
        Structure structure = new Structure();
        StructureElement element = new StructureElement();

        structure.setName(TEST_STRUCTURE_NAME);
        structure.setTag("0");
        structure.setDescription("0");
        structure.setElements(new ArrayList<>());
        structure.addElement(element);
        structure.setSignature(new PersistenceSignature("SOLODOUV"));

        Structure copy = service.createCopyStructure(structure, "COPY");
        Assert.assertNotNull(copy);
        Assert.assertEquals("COPY", copy.getName());
        Assert.assertEquals("0", copy.getTag());
        Assert.assertEquals("0", copy.getDescription());
        Assert.assertNotNull(copy.getElements());
        Assert.assertNotEquals(0, copy.getElements());
        Assert.assertNotNull(copy.getSignature());
    }
}
