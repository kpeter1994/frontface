package hu.cubix.cloud.api.controller;

import feign.FeignException;
import hu.cubix.cloud.call.api.BackgroundApi;
import hu.cubix.cloud.call.model.BackgroundExamRequest;
import hu.cubix.cloud.call.model.BackgroundExamResponse;
import hu.cubix.cloud.model.FrontfaceRequest;
import hu.cubix.cloud.model.FrontfaceResponse;
import hu.cubix.cloud.model.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/frontface")
public class FrontfaceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontfaceController.class);
    private final BackgroundApi api;
    private final String token;

    public FrontfaceController(BackgroundApi api,
                               @Value("${frontface.token}") String token) {
        this.api = api;
        this.token = token;
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse status() {
        LOGGER.info("Status requested");
        StatusResponse status = new StatusResponse("frontface", StringUtils.hasText(token),
                getIpAddress(), getHostname(), LocalDateTime.now());
        LOGGER.info("Returning status: {}", status);
        return status;
    }

    @GetMapping(path = "/call", produces = MediaType.APPLICATION_JSON_VALUE)
    public FrontfaceResponse call(@RequestParam(required = false, name = "message") String message) {
        LOGGER.info("Preparing for calling the background application with default token - message was: {}", message);
        return call(message, token, false);
    }

    @PostMapping(path = "/call", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FrontfaceResponse call(@RequestBody FrontfaceRequest request) {
        LOGGER.info("Preparing for calling the background application with custom token - message was: {}", request.message());
        return call(request.message(), request.token(), true);
    }

    private FrontfaceResponse call(String message, String callToken, boolean customTokenUsed) {
        LOGGER.info("Calling Background application");
        LocalDateTime start = LocalDateTime.now();
        try {
            BackgroundExamResponse backgroundResponse = api.exam(new BackgroundExamRequest(message, callToken));
            LocalDateTime end = LocalDateTime.now();
            LOGGER.info("Background call was successful, response was: {}", backgroundResponse);
            Duration timeBetween = Duration.between(start, end);
            FrontfaceResponse frontfaceResponse = new FrontfaceResponse(timeBetween.abs().toMillis(), message, customTokenUsed);
            LOGGER.info("Response will be: {}", frontfaceResponse);
            return frontfaceResponse;
        } catch (FeignException.Unauthorized exception) {
            LOGGER.error("The provided token was incorrect");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect token during background call");
        }
    }

    private String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception exception) {
            LOGGER.warn("Unable to determine IP address", exception);
            return null;
        }
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception exception) {
            LOGGER.warn("Unable to determine hostname", exception);
            return null;
        }
    }
}
