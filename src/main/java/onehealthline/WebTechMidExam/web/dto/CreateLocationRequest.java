package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLocationRequest(
		@NotBlank String sector,
		@NotBlank String cell,
		@NotBlank String village,
		@NotNull Long provinceId,
		@NotNull Long districtId
) {
}

