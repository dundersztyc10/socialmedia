package pl.dundersztyc.accountcreator.infrastructure.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.dundersztyc.accounts.dto.AccountRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dundersztyc.accountcreator.infrastructure.web.AccountTestData.defaultRequestWithUsername;

class AccountCreatorRegisterTest extends AbstractIntegrationTest {
    private static final String REGISTER_URL = "/api/v1/public/register";

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void shouldRegister() throws Exception {
        AccountRequest request = defaultRequestWithUsername("username");

        register(objectMapper.writeValueAsString(request))
                .andExpect(status().isCreated());
    }


    @Test
    public void cannotRegisterWhenUsernameExists() throws Exception {
        AccountRequest request = defaultRequestWithUsername("username");

        register(objectMapper.writeValueAsString(request))
                .andExpect(status().isCreated());
        register(objectMapper.writeValueAsString(request))
                .andExpect(status().isBadRequest());
    }


    private ResultActions register(String user) throws Exception {
        return mockMvc
                .perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user));
    }

}
