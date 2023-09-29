package pl.dundersztyc.accountcreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.dundersztyc.JwtConfig;
import pl.dundersztyc.RsaConfig;
import pl.dundersztyc.SecurityConfig;
import pl.dundersztyc.accountcreator.infrastructure.web.AccountCreatorController;
import pl.dundersztyc.application.SocialMediaApplication;

@SpringBootTest(classes = {SocialMediaApplication.class})
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest extends AbstractTestcontainers {

    @Autowired
    protected MockMvc mockMvc;

}
