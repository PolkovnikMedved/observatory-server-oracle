package be.solodoukhin.domain.tool;

import be.solodoukhin.ApplicationWithoutContextTest;
import be.solodoukhin.domain.api.ProjectIdentifier;
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
public class ProjectIdentifierTest extends ApplicationWithoutContextTest {

    private static final String PROJECT_NAME = "observatory-server";
    private static final String PROJECT_VERSION = "1.0.0";

    @Test
    public void testConstructor() {
        log.info("ProjectIdentifierTest.testConstructor()");
        ProjectIdentifier projectIdentifier = new ProjectIdentifier(PROJECT_NAME, PROJECT_VERSION);
        Assert.assertNotNull(projectIdentifier);
        Assert.assertEquals(PROJECT_NAME, projectIdentifier.getProjectName());
        Assert.assertEquals(PROJECT_VERSION, projectIdentifier.getProjectVersion());
    }
}
