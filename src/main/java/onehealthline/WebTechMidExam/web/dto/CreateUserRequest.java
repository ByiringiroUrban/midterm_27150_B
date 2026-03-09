package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
		@NotBlank String fullName,
		@NotBlank @Email String email,
		@NotBlank String phone,
		@NotNull Long provinceId,
		@NotNull Long districtId
) {
}

