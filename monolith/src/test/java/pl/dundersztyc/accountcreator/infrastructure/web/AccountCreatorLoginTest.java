package pl.dundersztyc.accountcreator.infrastructure.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.dundersztyc.accountcreator.dto.AuthRequest;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class AccountCreatorLoginTest extends AbstractIntegrationTest {

    private static final String REGISTER_URL = "/api/v1/public/register";
    private static final String LOGIN_URL = "/api/v1/public/login";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void shouldLogin() throws Exception {

        saveAccount(USERNAME, PASSWORD);

        AuthRequest authRequest = new AuthRequest(USERNAME, PASSWORD);

        MvcResult loginResult = login(authRequest)
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        AccountDto accountDto = objectMapper.readValue(loginResult.getResponse().getContentAsString(), AccountDto.class);

        assertThat(accountDto.username()).isEqualTo(authRequest.username());
    }


    @Test
    public void cannotLoginWhenUsernameIsIncorrect() throws Exception {

        saveAccount(USERNAME, PASSWORD);
        AuthRequest authRequest = new AuthRequest("incorrectUsername", PASSWORD);

        login(authRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andExpect(content().string(Matchers.blankString()));
    }

    @Test
    public void cannotLoginWhenPasswordIsIncorrect() throws Exception {

        saveAccount(USERNAME, PASSWORD);
        AuthRequest authRequest = new AuthRequest(USERNAME, "incorrectPassword");

        login(authRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andExpect(content().string(Matchers.blankString()));
    }


    private ResultActions login(AuthRequest authRequest) throws Exception {
        return mockMvc
                .perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)));
    }

    private void saveAccount(String username, String password) throws Exception {
        var accountRequest = new AccountRequest(username, "firstname", "lastname", password);
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
    }

}