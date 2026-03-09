package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePatientProfileRequest(
		@NotNull Long userId,
		@NotBlank String gender,
		@NotBlank String bloodGroup
) {
}

