package be.solodoukhin.service;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.StructureElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
public class ReorderElementsServiceTest extends ApplicationWithoutContextTest {
    private ReorderElementsService service = new ReorderElementsService();

    @Test
    public void testReorderElements() {
        LOGGER.info("ReorderElementsServiceTest.testReorderElements()");
        Structure structure = new Structure();
        StructureElement element01 = new StructureElement();
        element01.setSequence(1);

        StructureElement element02 = new StructureElement();
        element02.setSequence(2);

        StructureElement element03 = new StructureElement();
        element03.setSequence(3);

        StructureElement element04 = new StructureElement();
        element04.setSequence(4);

        structure.addElement(element04);
        structure.addElement(element03);
        structure.addElement(element01);
        structure.addElement(element02);

        // Be sure that the elements were placed in the order sequence = 4, 3, 1, 2
        int counter1 = 1;
        for(StructureElement e : structure.getElements()) {

            if(counter1 == 1) { Assert.assertEquals(4, e.getSequence()); }
            if(counter1 == 2) { Assert.assertEquals(3, e.getSequence()); }
            if(counter1 == 3) { Assert.assertEquals(1, e.getSequence()); }
            if(counter1 == 4) { Assert.assertEquals(2, e.getSequence()); }

            counter1++;
        }

        // reorder
        this.service.reorder(structure);

        // Assert that each element has been given a sequence from 0 to elements.size
        int counter2 = 0;
        for(StructureElement el: structure.getElements()) {

            Assert.assertEquals(counter2, el.getSequence());

            counter2++;
        }
    }

    @Test
    public void testWithoutElements() {
        LOGGER.info("ReorderElementsServiceTest.testWithoutElements()");
        Structure structure = new Structure();
        this.service.reorder(structure);
        Assert.assertEquals(new ArrayList<>(), structure.getElements());
    }
}
