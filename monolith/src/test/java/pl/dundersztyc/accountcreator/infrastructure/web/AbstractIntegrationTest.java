package pl.dundersztyc.accountcreator.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.dundersztyc.application.SocialMediaApplication;

@SpringBootTest(classes = SocialMediaApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@DirtiesContext
public abstract class AbstractIntegrationTest extends AbstractTestcontainers {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}