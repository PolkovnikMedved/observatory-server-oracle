package be.solodoukhin.domain;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.embeddable.Label;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class DocumentCategoryTest extends ApplicationWithoutContextTest {

    @Test
    public void testConstructor() {
        LOGGER.info("DocumentCategoryTest.testConstructor()");
        DocumentCategory documentCategory = new DocumentCategory();
        Assert.assertNotNull(documentCategory);
    }

    @Test
    public void testFields() {
        LOGGER.info("DocumentCategoryTest.testFields()");
        DocumentCategory category = new DocumentCategory();
        category.setNumber(0);
        category.setLabel(new Label());

        Assert.assertNotNull(category);
        Assert.assertEquals(0, (int) category.getNumber());
        Assert.assertNotNull(category.getLabel());
    }
}
