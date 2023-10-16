package pl.dundersztyc.accountcreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import pl.dundersztyc.accounts.dto.AccountRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dundersztyc.accountcreator.AccountTestData.defaultRequestWithUsername;

class AccountCreatorRegisterTest extends AbstractIntegrationTest {
    private static final String REGISTER_URL = "/api/v1/public/register";

    @Test
    public void shouldRegisterPass() throws Exception {

        AccountRequest request = defaultRequestWithUsername("username");

        ObjectMapper objectMapper = new ObjectMapper();

        var result = mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)));
        result
                .andExpect(status().isCreated());
    }

    /*
    @Test
    public void credentialsAreIncorrect() throws Exception {

        User user = defaultUser().build();
        String jsonUser = toJsonString(user);

        JSONObject incorrectUser = new JSONObject(toJsonString(user));
        incorrectUser.put("password", null);

        register(incorrectUser.toString())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cannotRegisterWhenUsernameExists() throws Exception {

        User user = defaultUser().build();
        saveUserPort.save(user);

        register(toJsonString(user))
                .andExpect(status().isBadRequest());
    }

    private ResultActions register(String user) throws Exception {
        return mockMvc
                .perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user));
    }

     */
}
