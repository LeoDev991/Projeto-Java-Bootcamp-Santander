package com.portfolio.commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank @Size(max = 120) String name,
        @Email @NotBlank @Size(max = 120) String email,
        @NotBlank @Pattern(regexp = "\\d{11}|\\d{14}", message = "document must have 11 or 14 digits") String document
) {
}
