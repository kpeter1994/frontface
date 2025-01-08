package hu.cubix.cloud.api.controller;

import feign.FeignException;
import hu.cubix.cloud.call.api.BackgroundApi;
import hu.cubix.cloud.call.model.BackgroundExamRequest;
import hu.cubix.cloud.call.model.BackgroundExamResponse;
import hu.cubix.cloud.model.FrontfaceRequest;
import hu.cubix.cloud.model.FrontfaceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FrontfaceControllerTest {

    private static final String TOKEN = "TESTTOKEN";
    @Mock
    BackgroundApi background;
    private FrontfaceController sut;

    @BeforeEach
    void setUp() {
        sut = new FrontfaceController(background, TOKEN);
    }

    @Test
    void callOnlyMessage() {
        String message = "testMessage";
        when(background.exam(new BackgroundExamRequest(message, TOKEN))).thenReturn(new BackgroundExamResponse(message));

        FrontfaceResponse response = sut.call(message);

        assertThat(response.message(), is(message));
        assertThat(response.customTokenUsed(), is(false));
    }

    @Test
    void callFull() {
        String message = "testMessage";
        when(background.exam(new BackgroundExamRequest(message, TOKEN))).thenReturn(new BackgroundExamResponse(message));

        FrontfaceResponse response = sut.call(new FrontfaceRequest(message, TOKEN));

        assertThat(response.message(), is(message));
        assertThat(response.customTokenUsed(), is(true));
    }

    @Test
    void callIncorrectToken() {
        String message = "testMessage";
        when(background.exam(new BackgroundExamRequest(message, TOKEN))).thenThrow(mock(FeignException.Unauthorized.class));
        FrontfaceRequest request = new FrontfaceRequest(message, TOKEN);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> sut.call(request));

        assertThat(exception.getStatusCode(), is(HttpStatusCode.valueOf(400)));
    }
}