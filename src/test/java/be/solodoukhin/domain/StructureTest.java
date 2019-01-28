package be.solodoukhin.domain;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
@Slf4j
public class StructureTest extends ApplicationWithoutContextTest {

    private static final String TEST_STRUCTURE_NAME = "UNIT_TEST";

    @Test
    public void testConstructor() {
        log.info("StructureTest.testConstructor()");
        Structure structure = new Structure();
        Assert.assertNotNull(structure);
    }

    @Test
    public void testFields() {
        log.info("StructureTest.testFields()");
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

    @Test
    public void testCopyConstructor() {
        log.info("StructureTest.testCopyConstructor()");

        Structure type = new Structure();
        type.setName("STRUCTURE_TYPE");

        StructureElement element = new StructureElement();
        element.setId(25L);
        element.setTypeStructure(type);

        Structure base = new Structure();
        base.setName(TEST_STRUCTURE_NAME);
        base.setTag("0");
        base.setDescription("0");
        base.setElements(new ArrayList<>());
        base.addElement(element);
        base.setSignature(new PersistenceSignature("SOLODOUV"));

        Structure copy = new Structure(base);
        Assert.assertNotNull(copy);
        Assert.assertEquals(TEST_STRUCTURE_NAME, copy.getName());
        Assert.assertEquals("0", copy.getTag());
        Assert.assertEquals("0", copy.getDescription());

        Assert.assertNotNull(copy.getSignature());
        Assert.assertEquals("SOLODOUV", copy.getSignature().getCreatedBy());

        Assert.assertNotNull(copy.getElements());
        Assert.assertNotEquals(0, copy.getElements());
        Assert.assertEquals(25L, ( long ) copy.getElements().get(0).getId());

        Assert.assertNotNull(copy.getElements().get(0).getTypeStructure());
        Assert.assertEquals("STRUCTURE_TYPE", copy.getElements().get(0).getTypeStructure().getName());
    }

    @Test
    public void testCompareToMethod() {
        Structure structure = new Structure();

        StructureElement element1 = new StructureElement();
        element1.setId(1L);
        element1.setSequence(1);

        StructureElement element2 = new StructureElement();
        element2.setId(2L);
        element2.setSequence(2);

        structure.addElement(element2);
        structure.addElement(element1);

        Assert.assertNotNull(structure);
        Assert.assertNotNull(structure.getElements());
        Assert.assertEquals(2,structure.getElements().size());

        Assert.assertEquals(2L, (long) structure.getElements().get(0).getId());
        Assert.assertEquals(2, structure.getElements().get(0).getSequence());

        Assert.assertEquals(1L, (long) structure.getElements().get(1).getId());
        Assert.assertEquals(1, structure.getElements().get(1).getSequence());

        Collections.sort(structure.getElements());

        Assert.assertEquals(1L, (long) structure.getElements().get(0).getId());
        Assert.assertEquals(1, structure.getElements().get(0).getSequence());

        Assert.assertEquals(2L, (long) structure.getElements().get(1).getId());
        Assert.assertEquals(2, structure.getElements().get(1).getSequence());

    }
}
