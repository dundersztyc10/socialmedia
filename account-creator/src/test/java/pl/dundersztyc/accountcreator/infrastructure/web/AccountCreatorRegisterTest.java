package pl.dundersztyc.accountcreator.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import pl.dundersztyc.RsaConfig;
import pl.dundersztyc.accountcreator.AbstractIntegrationTest;
import pl.dundersztyc.accounts.dto.AccountRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dundersztyc.accountcreator.infrastructure.web.AccountTestData.defaultRequestWithUsername;


class AccountCreatorRegisterTest extends AbstractIntegrationTest {

    private static final String REGISTER_URL = "/api/v1/public/register";


    @Test
    public void shouldRegisterPass() throws Exception {

        AccountRequest request = defaultRequestWithUsername("username");

        ObjectMapper objectMapper = new ObjectMapper();

        var result = mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
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