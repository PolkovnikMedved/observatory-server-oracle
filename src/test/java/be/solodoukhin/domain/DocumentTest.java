package be.solodoukhin.domain;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.DocumentCategory;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.Label;
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
public class DocumentTest extends ApplicationWithoutContextTest {

    @Test
    public void testConstructor() {
        log.info("DocumentTest.testConstructor()");
        Document document = new Document();
        Assert.assertNotNull(document);
    }

    @Test
    public void testFields() {
        log.info("DocumentTest.testFields()");
        Document document = new Document();
        Version version = new Version();

        document.setNumber(0);
        document.setCategory(new DocumentCategory());
        document.setLabel(new Label());
        document.setVersions(new ArrayList<>());
        document.getVersions().add(version);
        document.setSignature(new PersistenceSignature("SOLODOUV"));

        Assert.assertNotNull(document);
        Assert.assertEquals(0, (int) document.getNumber());
        Assert.assertNotNull(document.getVersions());
        Assert.assertNotEquals(0, document.getVersions().size());
    }
}
