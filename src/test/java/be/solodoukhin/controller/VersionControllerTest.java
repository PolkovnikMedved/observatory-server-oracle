package be.solodoukhin.controller;

import be.solodoukhin.ApplicationTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class VersionControllerTest extends ApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_00() {
        LOGGER.info("VersionControllerTest.emptyTest()");
    }
}
