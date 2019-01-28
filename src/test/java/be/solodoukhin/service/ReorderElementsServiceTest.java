package be.solodoukhin.service;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.dto.StructureElementDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
@Slf4j
public class ReorderElementsServiceTest extends ApplicationWithoutContextTest {
    private ReorderElementsService service = new ReorderElementsService();

    @Test
    public void testReorderElementsWithDTO() {
        log.info("Call to ReorderElementsServiceTest.testReorderElementsWithDTO()");
        Structure structure = new Structure();
        StructureDTO structureDTO = new StructureDTO();

        StructureElement element01 = new StructureElement();
        element01.setId(51L);
        element01.setSequence(1);
        StructureElementDTO elementDTO01 = new StructureElementDTO();
        elementDTO01.setId(51L);
        elementDTO01.setSequence(1);

        StructureElement element02 = new StructureElement();
        element02.setId(52L);
        element02.setSequence(2);
        StructureElementDTO elementDTO02 = new StructureElementDTO();
        elementDTO02.setId(52L);
        elementDTO02.setSequence(3);

        StructureElement element03 = new StructureElement();
        element03.setSequence(3);
        element03.setId(53L);
        StructureElementDTO elementDTO03 = new StructureElementDTO();
        elementDTO03.setId(53L);
        elementDTO03.setSequence(3);

        StructureElement element04 = new StructureElement();
        element04.setId(54L);
        element04.setSequence(4);
        StructureElementDTO elementDTO04 = new StructureElementDTO();
        elementDTO04.setId(54L);
        elementDTO04.setSequence(5);

        StructureElement element05 = new StructureElement();
        element05.setId(55L);
        element05.setSequence(5);
        StructureElementDTO elementDTO05 = new StructureElementDTO();
        elementDTO05.setId(55L);
        elementDTO05.setSequence(4);

        structure.addElement(element01);
        structure.addElement(element02);
        structure.addElement(element03);
        structure.addElement(element04);
        structure.addElement(element05);

        // Be sure that the elements were placed in the order sequence = 4, 3, 1, 2
        int counter1 = 1;
        for(StructureElement e : structure.getElements()) {
            if(counter1 == 1) { Assert.assertEquals(1, e.getSequence()); }
            if(counter1 == 2) { Assert.assertEquals(2, e.getSequence()); }
            if(counter1 == 3) { Assert.assertEquals(3, e.getSequence()); }
            if(counter1 == 4) { Assert.assertEquals(4, e.getSequence()); }
            if(counter1 == 5) { Assert.assertEquals(5, e.getSequence()); }

            counter1++;
        }

        structureDTO.addElement(elementDTO01);
        structureDTO.addElement(elementDTO02);
        structureDTO.addElement(elementDTO03);
        structureDTO.addElement(elementDTO04);
        structureDTO.addElement(elementDTO05);

        // Ensure that DTO elements have been placed in the right order
        int secondCounter = 1;
        for(StructureElementDTO dto : structureDTO.getElements()) {
            if(secondCounter == 1) { Assert.assertEquals(1 ,(long) dto.getSequence()); }
            if(secondCounter == 2) { Assert.assertEquals(3 ,(long) dto.getSequence()); }
            if(secondCounter == 3) { Assert.assertEquals(3 ,(long) dto.getSequence()); }
            if(secondCounter == 4) { Assert.assertEquals(5 ,(long) dto.getSequence()); }
            if(secondCounter == 5) { Assert.assertEquals(4 ,(long) dto.getSequence()); }
            secondCounter++;
        }

        this.service.reorder(structure, structureDTO);

        for(int i = 0; i < structure.getElements().size(); i++) {
            Assert.assertEquals(i, structure.getElements().get(i).getSequence());
        }
    }

    @Test
    public void testReorderWithoutElements() {
        log.info("Call to ReorderElementsServiceTest.testWithoutElements()");
        Structure structure = new Structure();
        StructureDTO dto = new StructureDTO();
        this.service.reorder(structure, dto);
        Assert.assertEquals(new ArrayList<>(), structure.getElements());
    }
}
