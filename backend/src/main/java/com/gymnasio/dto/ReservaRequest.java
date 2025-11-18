package com.gymnasio.dto;

import jakarta.validation.constraints.NotNull;

public record ReservaRequest(
        @NotNull Integer claseId) {
}