package hu.cubix.cloud.call.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class BackgroundApiMock {

    public static WireMockServer createMockServer() {
        return new WireMockServer(18080);
    }

    public static void setupMockResponses(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/background/exam"))
                .withRequestBody(new ContainsPattern("TEST-TOKEN"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        BackgroundApiMock.class.getClassLoader().getResourceAsStream("response/mock-message.json"),
                                        defaultCharset())
                        )));
    }
}
