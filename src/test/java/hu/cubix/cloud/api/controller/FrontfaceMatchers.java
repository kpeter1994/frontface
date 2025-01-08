package hu.cubix.cloud.api.controller;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;

class FrontfaceMatchers {
    private FrontfaceMatchers() {
    }

    static ResultMatcher createJsonPathMatcherForRoot(String fieldName, Object value) {
        return MockMvcResultMatchers.jsonPath("$." + fieldName, is(value));
    }

    static ResultMatcher createMsForReplyJsonPathMatcher() {
        return MockMvcResultMatchers.jsonPath("$.msForReply", greaterThan(-1L), Long.class);
    }

    static ResultMatcher createMessageJsonPathMatcher(String message) {
        return createJsonPathMatcherForRoot("message", message);
    }

    static ResultMatcher createApplicationNameJsonPathMatcher(String applicationName) {
        return createJsonPathMatcherForRoot("applicationName", applicationName);
    }

    static ResultMatcher createTokenSetupJsonPathMatcher(boolean isTokenSetup) {
        return createJsonPathMatcherForRoot("isTokenSetup", isTokenSetup);
    }

    static ResultMatcher createCustomTokenJsonPathMatcher(boolean customToken) {
        return createJsonPathMatcherForRoot("customTokenUsed", customToken);
    }

}
