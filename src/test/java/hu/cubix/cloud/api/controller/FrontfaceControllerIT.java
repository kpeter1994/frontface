package hu.cubix.cloud.api.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import hu.cubix.cloud.call.api.BackgroundApiMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class FrontfaceControllerIT {

    private static final String MESSAGE = "Hello";
    private static WireMockServer backgroundServer;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() throws IOException {
        backgroundServer = BackgroundApiMock.createMockServer();
        BackgroundApiMock.setupMockResponses(backgroundServer);
        backgroundServer.start();
    }
    @AfterAll
    static void afterAll() {
        backgroundServer.stop();
    }

    @Test
    void status() throws Exception {
        mockMvc.perform(get("/frontface/status"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(FrontfaceMatchers.createApplicationNameJsonPathMatcher("frontface"))
                .andExpect(FrontfaceMatchers.createTokenSetupJsonPathMatcher(true));
    }

    @Test
    void call_get() throws Exception {
        mockMvc.perform(get("/frontface/call").queryParam("message", MESSAGE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(FrontfaceMatchers.createMsForReplyJsonPathMatcher())
                .andExpect(FrontfaceMatchers.createMessageJsonPathMatcher(MESSAGE))
                .andExpect(FrontfaceMatchers.createCustomTokenJsonPathMatcher(false));
    }

    @Test
    void call_post() throws Exception {
        mockMvc.perform(post("/frontface/call")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        {
                            "message": "Hello",
                            "token": "TEST-TOKEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(FrontfaceMatchers.createMsForReplyJsonPathMatcher())
                .andExpect(FrontfaceMatchers.createMessageJsonPathMatcher(MESSAGE))
                .andExpect(FrontfaceMatchers.createCustomTokenJsonPathMatcher(true));
    }
}