package be.solodoukhin.domain.persistent.embeddable;

import be.solodoukhin.ApplicationWithoutContextTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.09
 */
public class LabelTest extends ApplicationWithoutContextTest {

    @Test
    public void testConstructor() {
        LOGGER.info("LabelTest.testConstructor()");
        Label label = new Label();
        Assert.assertNotNull(label);
    }

    @Test
    public void testFields() {
        String frenchLabel = "label_FR";
        String dutchLabel = "label_NL";
        String germanLabel = "label_DE";
        String englishLabel = "Label_EN";

        Label label = new Label(frenchLabel, dutchLabel, englishLabel, germanLabel);
        Assert.assertNotNull(label);
        Assert.assertEquals(frenchLabel, label.getFrenchLabel());
        Assert.assertEquals(dutchLabel, label.getDutchLabel());
        Assert.assertEquals(germanLabel, label.getGermanLabel());
        Assert.assertEquals(englishLabel, label.getEnglishLabel());
    }
}
