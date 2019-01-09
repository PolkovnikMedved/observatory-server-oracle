package be.groups.domain;

import be.groups.AbstractConfiguredTest;
import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.StructureElement;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class StructureTest extends AbstractConfiguredTest {

    private static final String TEST_STRUCTURE_NAME = "UNIT_TEST";

    @Test
    public void testConstructor() {
        LOGGER.info("StructureTest.testConstructor()");
        Structure structure = new Structure();
        Assert.assertNotNull(structure);
    }

    @Test
    public void testFields() {
        LOGGER.info("StructureTest.testFields()");
        Structure structure = new Structure();
        StructureElement element = new StructureElement();

        structure.setName(TEST_STRUCTURE_NAME);
        structure.setTag("0");
        structure.setDescription("0");
        structure.setElements(new ArrayList<>());
        structure.addElement(element);
        structure.setSignature(new PersistenceSignature("SOLODOUV"));

        Assert.assertNotNull(structure);
        Assert.assertEquals(TEST_STRUCTURE_NAME, structure.getName());
        Assert.assertEquals("0", structure.getTag());
        Assert.assertEquals("0", structure.getDescription());
        Assert.assertNotNull(structure.getElements());
        Assert.assertNotEquals(0, structure.getElements());
    }
}
