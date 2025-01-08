package hu.cubix.cloud.call.model;

import java.time.LocalDateTime;

public record StatusResponse(String applicationName, boolean isTokenSetup,
                             String ipAddress, String hostname, LocalDateTime statusTime) {
}
