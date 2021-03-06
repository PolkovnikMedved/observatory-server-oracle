package be.solodoukhin.domain;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
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
public class StructureElementTest extends ApplicationWithoutContextTest {

    @Test
    public void testConstructor() {
        log.info("StructureElementTest.testConstructor()");
        StructureElement structureElement = new StructureElement();
        Assert.assertNotNull(structureElement);
    }

    @Test
    public void testFields() {
        log.info("StructureElementTest.testFields()");
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

    @Test
    public void testCopyConstructor() {
        log.info("StructureElementTest.testCopyConstructor()");
        Structure type = new Structure();
        type.setName("TYPE");

        StructureElement base = new StructureElement();
        base.setId(525L);
        base.setTag("0");
        base.setDescription("0");
        base.setOptional(false);
        base.setRepetitive(false);
        base.setSequence(0);
        base.setTypeStructure(type);
        base.setSignature(new PersistenceSignature("SOLODOUV"));

        StructureElement copy = new StructureElement(base);
        Assert.assertNotNull(copy);
        Assert.assertEquals(525L, ( long ) copy.getId());
        Assert.assertTrue(copy.getTag().isPresent());
        Assert.assertEquals("0", copy.getTag().get());
        Assert.assertEquals("0", copy.getDescription());
        Assert.assertFalse(copy.isOptional());
        Assert.assertFalse(copy.isRepetitive());
        Assert.assertEquals(0, copy.getSequence());

        Assert.assertNotNull(copy.getTypeStructure());
        Assert.assertEquals("TYPE", copy.getTypeStructure().getName());

        Assert.assertNotNull(copy.getSignature());
        Assert.assertEquals("SOLODOUV", copy.getSignature().getCreatedBy());
    }
}
