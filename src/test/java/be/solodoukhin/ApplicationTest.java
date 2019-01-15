package be.solodoukhin;

import be.solodoukhin.tool.TnsAdmin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    protected static ObjectMapper mapper;

    @BeforeClass
    public static void test_setUpTNS() {
        TnsAdmin.init();
        LOGGER.info("TNS : " + System.getProperty("oracle.net.tns_admin"));
    }

    @BeforeClass
    public static void test_setUpObjectMapper() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new Jdk8Module()); // Workaround for Optional fields
        mapper.registerModule(new JavaTimeModule()); // Workaround for LocalDate fields
    }
}
