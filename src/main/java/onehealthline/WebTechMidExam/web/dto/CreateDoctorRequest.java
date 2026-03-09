package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateDoctorRequest(
		@NotBlank String fullName,
		@NotBlank @Email String email,
		@NotNull Long clinicId,
		@NotEmpty Set<Long> specialtyIds
) {
}

