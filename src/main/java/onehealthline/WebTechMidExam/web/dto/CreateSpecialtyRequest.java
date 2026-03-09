package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSpecialtyRequest(
		@NotBlank String name
) {
}

