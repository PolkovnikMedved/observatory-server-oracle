package be.solodoukhin.controller;

import be.solodoukhin.ApplicationTest;
import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.dto.StructureElementDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class StructuresControllerTest extends ApplicationTest {

    private final String STRUCTURE_NAME = "UNIT_TEST";
    private final String COPY_STRUCTURE_NAME = "UNIT_TEST_COPY";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StructureRepository repository;

    @Test
    public void test_01_createStructure() {
        LOGGER.info("StructuresControllerTest.test_01_createStructure()");
        Structure structure = new Structure();
        structure.setName(STRUCTURE_NAME);
        structure.setTag("00");
        structure.setDescription("00");
        structure.setSignature(new PersistenceSignature(STRUCTURE_NAME));

        StructureElement element01 = new StructureElement();
        element01.setTag("11");
        element01.setDescription("11");
        element01.setRepetitive(false);
        element01.setOptional(true);
        element01.setSequence(255);
        element01.setSignature(new PersistenceSignature(STRUCTURE_NAME));
        element01.getSignature().setModification(STRUCTURE_NAME);
        structure.addElement(element01);

        StructureElement element02 = new StructureElement();
        element02.setTag("22");
        element02.setDescription("22");
        element02.setRepetitive(false);
        element02.setOptional(true);
        element02.setSequence(21);
        element02.setSignature(new PersistenceSignature(STRUCTURE_NAME));
        element02.getSignature().setModification(STRUCTURE_NAME);
        structure.addElement(element02);

        this.repository.save(structure);
    }

    @Test
    @Transactional
    public void test_02_ensureStructureExists() {
        LOGGER.info("StructuresControllerTest.test_02_ensureStructureExists()");
        Optional<Structure> savedStructure = this.repository.findById(STRUCTURE_NAME);

        Assert.assertTrue(savedStructure.isPresent());
        Assert.assertNotNull(savedStructure.get().getElements());
        Assert.assertEquals("00", savedStructure.get().getTag());
        Assert.assertEquals("00", savedStructure.get().getDescription());
        Assert.assertEquals(2, savedStructure.get().getElements().size());
    }

    @Test
    public void test_03_testGetAllStructureNames() throws Exception {
        LOGGER.info("StructuresControllerTest.test_03_testGetAllStructureNames()");
        mvc.perform(MockMvcRequestBuilders
                .get("/structure/all-names"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    public void test_04_testReadOneStructureEndpoint() throws Exception {
        LOGGER.info("StructuresControllerTest.test_04_testReadOneStructureEndpoint()");
        mvc.perform(MockMvcRequestBuilders
                .get("/structure/" + STRUCTURE_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(STRUCTURE_NAME));
    }

    @Test
    public void test_05_testStructureIsUsed() throws Exception {
        LOGGER.info("StructuresControllerTest.test_05_testStructureIsUsed()");
        mvc.perform(MockMvcRequestBuilders
                .get("/structure/is-used?name=" + STRUCTURE_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    public void test_06_testReadAllStructures() throws Exception {
        LOGGER.info("StructuresControllerTest.test_06_testReadAllStructures()");
        mvc.perform(MockMvcRequestBuilders.get("/structure/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").isNumber());
    }

    @Test
    public void test_07_testReadAllStructuresFiltered() throws Exception {
        LOGGER.info("StructuresControllerTest.test_07_testReadAllStructuresFiltered()");
        mvc.perform(MockMvcRequestBuilders.get("/structure/all?name=" + STRUCTURE_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value("1"));
    }

    @Test
    public void test_08_testCopyStructure() throws Exception {
        LOGGER.info("StructuresControllerTest.test_08_testCopyStructure()");
        mvc.perform(MockMvcRequestBuilders.get("/structure/copy?from=" + STRUCTURE_NAME + "&to=" + COPY_STRUCTURE_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(COPY_STRUCTURE_NAME));
    }

    @Test
    @Transactional
    public void test_09_testUpdateOrder() throws Exception {
        LOGGER.info("StructuresControllerTest.test_09_testUpdateOrder()");
        Structure structure = this.repository.getOne(STRUCTURE_NAME);
        Assert.assertNotNull(structure);

        StructureDTO dto = new StructureDTO(structure);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.put("/structure/update-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].sequence").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].sequence").value("0"));
    }

    @Test
    @Transactional
    public void test_10_testAddElement() throws Exception {
        LOGGER.info("StructuresControllerTest.test_10_testAddElement()");
        Structure structure = this.repository.getOne(STRUCTURE_NAME);
        Assert.assertNotNull(structure);

        structure.setTag("99");

        StructureElement newElement = new StructureElement();
        newElement.setTag("33");
        newElement.setDescription("33");
        newElement.setSequence(3);
        newElement.setOptional(false);
        newElement.setRepetitive(true);
        newElement.setSignature(new PersistenceSignature(STRUCTURE_NAME));
        newElement.getSignature().setModification(STRUCTURE_NAME);

        structure.addElement(newElement);

        StructureDTO dto = new StructureDTO(structure);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.put("/structure/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tag").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tag").value("99"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.elements").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.elements[2]").exists());
    }

    @Test
    @Transactional
    public void test_11_testRemoveElement() throws Exception {
        LOGGER.info("StructuresControllerTest.test_11_testRemoveElement()");
        Structure structure = this.repository.getOne(STRUCTURE_NAME);
        Assert.assertNotNull(structure);
        Assert.assertNotNull(structure.getElements());
        Assert.assertTrue(structure.getElements().size() >= 1);

        structure.getElements().remove(1);
        structure.setTag("99");

        StructureDTO dto = new StructureDTO(structure);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.put("/structure/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tag").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tag").value("99"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[1]").doesNotExist());
    }

    // This method is in this class but should be moved into a StructureElementControllerTest
    // When we will have more things to test.
    @Test
    @Transactional
    public void test_50_testUpdateStructureElement() throws Exception {
        LOGGER.info("StructuresControllerTest.test_50_testUpdateStructureElement()");
        Structure structure = this.repository.getOne(STRUCTURE_NAME);
        Assert.assertNotNull(structure);
        Assert.assertNotNull(structure.getElements());
        Assert.assertNotEquals(0, structure.getElements().size());

        StructureElement element = structure.getElements().get(0);
        element.setTag("50");

        StructureElementDTO dto = new StructureElementDTO(element);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(dto);

        mvc.perform(
                MockMvcRequestBuilders.put("/structure-element/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tag").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tag").value("50"));
    }

    @Test
    public void test_99_cleanUpDatabase() {
        LOGGER.info("StructuresControllerTest.test_99_cleanUpDatabase()");
        this.repository.deleteById(STRUCTURE_NAME);
        this.repository.deleteById(COPY_STRUCTURE_NAME);

        Assert.assertFalse(this.repository.findById(STRUCTURE_NAME).isPresent());
        Assert.assertFalse(this.repository.findById(COPY_STRUCTURE_NAME).isPresent());
    }
}
