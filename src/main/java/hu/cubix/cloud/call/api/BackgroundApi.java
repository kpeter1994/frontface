package hu.cubix.cloud.call.api;

import hu.cubix.cloud.call.model.BackgroundExamRequest;
import hu.cubix.cloud.call.model.BackgroundExamResponse;
import hu.cubix.cloud.call.model.StatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "background", url = "${background.url:http://localhost}", path = "/background")
public interface BackgroundApi {

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    StatusResponse status();

    @PostMapping(path = "/exam", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    BackgroundExamResponse exam(BackgroundExamRequest request);
}
