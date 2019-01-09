package be.groups.domain;

import be.groups.AbstractConfiguredTest;
import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.StructureElement;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class StructureElementTest extends AbstractConfiguredTest {

    @Test
    public void testConstructor() {
        LOGGER.info("StructureElementTest.testConstructor()");
        StructureElement structureElement = new StructureElement();
        Assert.assertNotNull(structureElement);
    }

    @Test
    public void testFields() {
        LOGGER.info("StructureElementTest.testFields()");
        StructureElement structureElement = new StructureElement();
        structureElement.setId(525L);
        structureElement.setTag("0");
        structureElement.setDescription("0");
        structureElement.setOptional(false);
        structureElement.setRepetitive(false);
        structureElement.setSequence(0);
        structureElement.setTypeStructure(new Structure());
        structureElement.setSignature(new PersistenceSignature("SOLODOUV"));

        Assert.assertNotNull(structureElement);
        Assert.assertEquals(525L, (long) structureElement.getId());
        Assert.assertTrue(structureElement.getTag().isPresent());
        Assert.assertEquals("0", structureElement.getTag().get());
        Assert.assertEquals("0", structureElement.getDescription());
        Assert.assertFalse(structureElement.isOptional());
        Assert.assertFalse(structureElement.isRepetitive());
        Assert.assertEquals(0, structureElement.getSequence());
        Assert.assertNotNull(structureElement.getTypeStructure());
        Assert.assertNotNull(structureElement.getSignature());
    }
}
