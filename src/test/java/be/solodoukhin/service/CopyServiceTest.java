package be.solodoukhin.service;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
@Slf4j
public class CopyServiceTest extends ApplicationWithoutContextTest {

    private static final String VERSION_TEST_NAME = "UNIT_TEST";
    private static final String TEST_STRUCTURE_NAME = "UNIT_TEST";

    private CopyService service = new CopyService();

    @Test
    public void testDeepVersionCopy()
    {
        log.info("CopyServiceTest.testDeepVersionCopy()");
        Version version = new Version();
        version.setName(VERSION_TEST_NAME);
        version.setDfaName("0");
        version.setDescription("0");
        version.setStructure(new Structure());
        version.setSignature(new PersistenceSignature());

        Version copy = service.createCopyVersion(version, "COPY");
        Assert.assertNotNull(copy);
        Assert.assertEquals("COPY", copy.getName());
        Assert.assertTrue(copy.getDfaName().isPresent());
        Assert.assertEquals("0", copy.getDfaName().get());
        Assert.assertEquals("0", copy.getDescription());
        Assert.assertNotNull(copy.getStructure());
        Assert.assertNull(copy.getSignature());
    }

    @Test
    public void testDeepStructureCopy() {
        log.info("CopyServiceTest.testDeepStructureCopy()");
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
