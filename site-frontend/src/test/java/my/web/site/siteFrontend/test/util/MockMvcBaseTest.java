package my.web.site.siteFrontend.test.util;

import my.web.site.siteFrontend.SiteFrontendApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiteFrontendApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class MockMvcBaseTest {

    private Logger LOG = LoggerFactory.getLogger(MockMvcBaseTest.class);

    @Autowired
    public MockMvc mvc;
    @Autowired
    public TestDataUtil testDataUtil;

    @Before
    public void dataSetup() throws IOException {
        testDataUtil.setup();
    }

    @After
    public void dataClean(){
        testDataUtil.clean();
    }

    public void dummy() throws Exception {
        mvc.perform(post("")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andDo(r -> LOG.info(r.getResponse().toString()))
                .andExpect(status().isOk());//
    }



}
