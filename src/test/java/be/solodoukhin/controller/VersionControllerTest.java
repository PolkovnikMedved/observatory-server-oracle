package be.solodoukhin.controller;

import be.solodoukhin.ApplicationTest;
import be.solodoukhin.domain.dto.VersionDTO;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.DocumentCategory;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.Label;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
import be.solodoukhin.repository.VersionRepository;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class VersionControllerTest extends ApplicationTest {

    private static final int TEST_DOCUMENT_CATEGORIE_NUMBER = 99999;
    private static final int TEST_DOCUMENT_NUMBER = 99999;
    private static final String TEST_VERSION_NAME = "UNIT_TEST";
    private static final String TEST_STRUCTURE_NAME = "UNIT_TEST";
    private static final String TEST_VERSION_COPY_NAME = "UNIT_TEST_COPY";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private VersionRepository versionRepository;

    @Test
    public void test_00_createVersion() {
        log.info("VersionControllerTest.test_00_createVersion()");
        Document document = new Document();
        document.setNumber(TEST_DOCUMENT_NUMBER);
        document.setMandatoryFlag(0);
        document.setAtLeastOneCopy(0);
        document.setUniqueCopy(0);
        document.setLabel(new Label("TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE"));
        document.setSignature(new PersistenceSignature("TEST_UNIT"));
        document.getSignature().setModification("TEST_UNIT");

        DocumentCategory documentCategory = new DocumentCategory();
        documentCategory.setNumber(TEST_DOCUMENT_CATEGORIE_NUMBER);
        documentCategory.setLabel(new Label("TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE"));
        document.setCategory(documentCategory);

        Version version = new Version();
        version.setName(TEST_VERSION_NAME);
        version.setDfaName("0");
        version.setDescription("0");
        version.setSignature(new PersistenceSignature(TEST_VERSION_NAME));
        version.getSignature().setModification(TEST_VERSION_NAME);
        document.addVersion(version);

        Structure structure = new Structure();
        structure.setName(TEST_STRUCTURE_NAME);
        structure.setTag("0");
        structure.setDescription("0");
        structure.setSignature(new PersistenceSignature(TEST_STRUCTURE_NAME));
        structure.getSignature().setModification(TEST_STRUCTURE_NAME);
        version.setStructure(structure);

        this.documentRepository.save(document);
    }

    @Test
    public void test_01_readOneVersion() {
        log.info("VersionControllerTest.test_01_readOneVersion()");
        Assert.assertTrue(this.versionRepository.existsById(TEST_VERSION_NAME));
    }

    @Test
    public void test_02_testGetOneEndpoint() throws Exception {
        log.info("VersionControllerTest.test_02_testGetOneEndpoint()");
        mvc.perform(MockMvcRequestBuilders
                .get("/version/" + TEST_VERSION_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    @Transactional
    public void test_03_testCopyVersion() throws Exception
    {
        log.info("VersionControllerTest.test_03_testCopyVersion()");
        Optional<Version> version = this.versionRepository.findById(TEST_VERSION_NAME);
        Assert.assertTrue(version.isPresent());

        mvc.perform(MockMvcRequestBuilders.get("/version/copy?from=" + TEST_VERSION_NAME + "&to=" + TEST_VERSION_COPY_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.versions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.versions[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.versions[1].name").value(TEST_VERSION_COPY_NAME));
    }

    @Test
    @Transactional
    public void test_04_testUpdateVersion() throws Exception
    {
        log.info("VersionControllerTest.test_04_testUpdateVersion()");
        Optional<Version> version = this.versionRepository.findById(TEST_VERSION_NAME);
        Assert.assertTrue(version.isPresent());

        version.get().setDfaName("99");
        version.get().setDescription("000");

        VersionDTO versionDTO = new VersionDTO(version.get());

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(versionDTO);

        mvc.perform(MockMvcRequestBuilders
                .put("/version/update").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dfaName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dfaName").value("99"));
    }

    @Test
    public void test_99_cleanUpDatabase() {
        log.info("VersionControllerTest.test_99_cleanUpDatabase()");
        this.documentRepository.deleteById(TEST_DOCUMENT_NUMBER);

        Assert.assertFalse(this.documentRepository.existsById(TEST_DOCUMENT_NUMBER));
        Assert.assertFalse(this.versionRepository.existsById(TEST_VERSION_NAME));
    }
}
