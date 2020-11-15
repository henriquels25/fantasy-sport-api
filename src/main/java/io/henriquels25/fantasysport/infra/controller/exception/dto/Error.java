package io.henriquels25.fantasysport.infra.controller.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Error {

    private final String code;
    private final String description;
    private final List<ErrorDetail> details;

    @AllArgsConstructor
    @Getter
    public static class ErrorDetail {
        private final String name;
        private final String description;
    }

}
