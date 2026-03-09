package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProvinceRequest(
		@NotBlank String code,
		@NotBlank String name
) {
}

