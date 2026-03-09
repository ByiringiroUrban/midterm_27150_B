package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.AppUser;
import onehealthline.WebTechMidExam.domain.PatientProfile;
import onehealthline.WebTechMidExam.repo.PatientProfileRepository;
import onehealthline.WebTechMidExam.repo.UserRepository;
import onehealthline.WebTechMidExam.web.dto.CreatePatientProfileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patient-profiles")
public class PatientProfileController {

	private final PatientProfileRepository patientProfileRepository;
	private final UserRepository userRepository;

	public PatientProfileController(PatientProfileRepository patientProfileRepository, UserRepository userRepository) {
		this.patientProfileRepository = patientProfileRepository;
		this.userRepository = userRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PatientProfile create(@Valid @RequestBody CreatePatientProfileRequest req) {
		AppUser user = userRepository.findById(req.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId()));

		PatientProfile profile = new PatientProfile();
		profile.setUser(user);
		profile.setGender(req.gender());
		profile.setBloodGroup(req.bloodGroup());

		return patientProfileRepository.save(profile);
	}

	@GetMapping
	public List<PatientProfile> list() {
		return patientProfileRepository.findAll();
	}

	@GetMapping("/by-user/{userId}")
	public PatientProfile byUser(@PathVariable Long userId) {
		return patientProfileRepository.findByUser_Id(userId)
				.orElseThrow(() -> new IllegalArgumentException("Profile not found for user: " + userId));
	}
}

