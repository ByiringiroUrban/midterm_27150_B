package onehealthline.WebTechMidExam.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
		@NotNull Long patientUserId,
		@NotNull Long doctorId,
		@NotNull LocalDateTime scheduledAt
) {
}

