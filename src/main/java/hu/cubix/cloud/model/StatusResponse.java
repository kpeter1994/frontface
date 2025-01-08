package hu.cubix.cloud.model;

import java.time.LocalDateTime;

public record StatusResponse(String applicationName, boolean isTokenSetup,
                             String ipAddress, String hostname, LocalDateTime statusTime) {
}
