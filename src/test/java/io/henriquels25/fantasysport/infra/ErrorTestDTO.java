package io.henriquels25.fantasysport.infra;

import java.util.List;
import java.util.Objects;

public class ErrorTestDTO {
    private String code;
    private String description;
    private List<ErrorTestDTO.ErrorTestDetailDTO> details;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ErrorTestDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ErrorTestDetailDTO> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorTestDTO that = (ErrorTestDTO) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(description, that.description) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description, details);
    }

    public static class ErrorTestDetailDTO {
        private String name;
        private String description;

        public ErrorTestDetailDTO() {
        }

        public ErrorTestDetailDTO(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ErrorTestDetailDTO that = (ErrorTestDetailDTO) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, description);
        }
    }
}
