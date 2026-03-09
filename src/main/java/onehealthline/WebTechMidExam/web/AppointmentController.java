package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.AppUser;
import onehealthline.WebTechMidExam.domain.Appointment;
import onehealthline.WebTechMidExam.domain.Doctor;
import onehealthline.WebTechMidExam.repo.AppointmentRepository;
import onehealthline.WebTechMidExam.repo.DoctorRepository;
import onehealthline.WebTechMidExam.repo.UserRepository;
import onehealthline.WebTechMidExam.web.dto.CreateAppointmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	private final AppointmentRepository appointmentRepository;
	private final UserRepository userRepository;
	private final DoctorRepository doctorRepository;

	public AppointmentController(
			AppointmentRepository appointmentRepository,
			UserRepository userRepository,
			DoctorRepository doctorRepository
	) {
		this.appointmentRepository = appointmentRepository;
		this.userRepository = userRepository;
		this.doctorRepository = doctorRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Appointment create(@Valid @RequestBody CreateAppointmentRequest req) {
		AppUser patient = userRepository.findById(req.patientUserId())
				.orElseThrow(() -> new IllegalArgumentException("Patient not found: " + req.patientUserId()));
		Doctor doctor = doctorRepository.findById(req.doctorId())
				.orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + req.doctorId()));

		Appointment appt = new Appointment();
		appt.setPatient(patient);
		appt.setDoctor(doctor);
		appt.setScheduledAt(req.scheduledAt());

		return appointmentRepository.save(appt);
	}

	@GetMapping
	public List<Appointment> list() {
		return appointmentRepository.findAll();
	}

	@GetMapping("/page")
	public Page<Appointment> listPaginated(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "scheduledAt") String sortBy,
			@RequestParam(defaultValue = "asc") String direction
	) {
		Sort sort = "desc".equalsIgnoreCase(direction)
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return appointmentRepository.findAll(pageable);
	}

	@GetMapping("/by-patient/{userId}")
	public List<Appointment> byPatient(@PathVariable Long userId) {
		return appointmentRepository.findByPatient_Id(userId);
	}

	@GetMapping("/by-doctor/{doctorId}")
	public List<Appointment> byDoctor(@PathVariable Long doctorId) {
		return appointmentRepository.findByDoctor_Id(doctorId);
	}
}

