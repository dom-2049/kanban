package integration.security;

import com.dgsystems.kanban.Application;
import com.dgsystems.kanban.presenters.getBoard.Board;
import com.dgsystems.kanban.usecases.CreateBoardRequest;
import com.dgsystems.kanban.web.JwtRequest;
import com.dgsystems.kanban.web.UserAccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@Transactional
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class BoardControllerTest {
    // https://www.baeldung.com/oauth-api-testing-with-spring-mvc

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper;

    @Test
    @DisplayName("Should register user")
    void shouldRegisterUser() throws Exception {

        UserAccountDTO user = new UserAccountDTO();
        user.setUsername("user");
        user.setPassword("password");

        registerUser(user);
    }

    @Test
    @DisplayName("Should obtain bearer token")
    void shouldObtainBearerToken() throws Exception {
        UserAccountDTO user = new UserAccountDTO();
        user.setUsername("user");
        user.setPassword("password");

        registerUser(user);

        String bearerToken = obtainToken(user);
        assertThat(bearerToken).isNotBlank();
    }

    @Test
    @DisplayName("Authenticated user should create board")
    void authenticatedUserShouldCreateBoard() throws Exception {
        UserAccountDTO user = new UserAccountDTO();
        user.setUsername("user");
        user.setPassword("password");

        registerUser(user);

        String bearerToken = obtainToken(user);
        CreateBoardRequest createBoardRequest = new CreateBoardRequest("work");

        mvcMockAuthorizedPost("/board", bearerToken, mapper.writeValueAsString(createBoardRequest), () -> status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/board/work")
                        .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Board board = mapper.readValue(contentAsString, Board.class);
        assertThat(board).isNotNull();
    }

    @Test
    @DisplayName("Give no token should be unauthorized to create board")
    void giveNoTokenShouldBeUnauthorizedToCreateBoard() throws Exception {
        CreateBoardRequest createBoardRequest = new CreateBoardRequest("work");
        mvcMockPost("/board", mapper.writeValueAsString(createBoardRequest), () -> status().isUnauthorized());
    }

    private String obtainToken(UserAccountDTO user) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", user.getUsername());
        params.add("password", user.getPassword());

        JwtRequest request = new JwtRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());

        ResultActions result = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request))
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }

    private void registerUser(UserAccountDTO user) throws Exception {
        mvcMockPost("/register", mapper.writeValueAsString(user), () -> status().isOk());
    }

    private ResultActions mvcMockPost(String urlTemplate, String content, Supplier<ResultMatcher> expected) throws Exception {
        return mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(expected.get());
    }

    private void mvcMockAuthorizedPost(String urlTemplate, String bearerToken, String content, Supplier<ResultMatcher> expected) throws Exception {
        mockMvc.perform(post(urlTemplate)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
        ).andExpect(expected.get());
    }

    @BeforeAll
    void setUp() {
        mapper = new ObjectMapper();
    }
}
