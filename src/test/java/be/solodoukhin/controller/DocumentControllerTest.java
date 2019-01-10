package be.solodoukhin.controller;

import be.solodoukhin.ApplicationTest;
import be.solodoukhin.domain.Document;
import be.solodoukhin.domain.DocumentCategory;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.Label;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
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

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class DocumentControllerTest extends ApplicationTest {

    private final int testDocumentNumber = 99999;
    private final int testDocumentCategoryNumber = 99999;
    private final String testVersionName = "TEST_UNITAIRE";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void test_00_createTestDocument() {
        LOGGER.info("DocumentControllerTest.test_00_createTestDocument()");
        Document document = new Document();
        document.setNumber(this.testDocumentNumber);
        document.setLabel(new Label("TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE"));
        document.setSignature(new PersistenceSignature("TEST_UNIT"));

        Version version = new Version();
        version.setName(this.testVersionName);
        version.setDescription("TEST_UNITAIRE");
        version.setSignature(new PersistenceSignature("TEST_UNIT"));
        version.getSignature().setModification(version.getSignature().getCreatedBy());
        document.addVersion(version);

        DocumentCategory documentCategory = new DocumentCategory();
        documentCategory.setNumber(testDocumentCategoryNumber);
        documentCategory.setLabel(new Label("TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE", "TEST_UNITAIRE"));
        document.setCategory(documentCategory);

        // Should save document and version
        documentRepository.save(document);
    }

    @Test
    @Transactional
    public void test_01_readCreatedDocument() {
        LOGGER.info("DocumentControllerTest.test_01_readCreatedDocument()");
        Document savedDocument = this.documentRepository.getOne(this.testDocumentNumber);

        System.out.println("Saved document " +  savedDocument);
        Assert.assertNotNull(savedDocument);
        Assert.assertNotNull(savedDocument.getVersions());
        Assert.assertEquals(1, savedDocument.getVersions().size());
        Assert.assertEquals(testVersionName, savedDocument.getVersions().get(0).getName());
        Assert.assertNotNull(savedDocument.getCategory());
        Assert.assertEquals(testDocumentCategoryNumber, ( int ) savedDocument.getCategory().getNumber());
    }

    @Test
    public void test_03_testGetDocumentEndpoint() throws Exception {
        LOGGER.info("DocumentControllerTest.test_03_testGetDocumentEndpoint()");
        mvc.perform(MockMvcRequestBuilders
                .get("/document/" + this.testDocumentNumber))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    public void test_02_updateVersion() {
        LOGGER.info("DocumentControllerTest.test_01_updateVersion()");
    }

    @Test
    public void test_99_cleanDatabase() {
        LOGGER.info("DocumentControllerTest.test_99_cleanDatabase()");
        this.documentRepository.deleteById(this.testDocumentNumber);

        Assert.assertFalse(this.documentRepository.findById(this.testDocumentNumber).isPresent());
    }
}
