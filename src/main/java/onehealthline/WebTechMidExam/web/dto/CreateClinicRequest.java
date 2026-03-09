package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClinicRequest(
		@NotBlank String name,
		@NotNull Long provinceId,
		@NotNull Long districtId
) {
}

