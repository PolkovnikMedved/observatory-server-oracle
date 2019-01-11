package be.solodoukhin;

import be.solodoukhin.tool.TnsAdmin;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class ApplicationTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

    @BeforeClass
    public static void test_setUpTNS() {
        TnsAdmin.init();
        LOGGER.info("TNS : " + System.getProperty("oracle.net.tns_admin"));
    }
}
