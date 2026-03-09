package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.Clinic;
import onehealthline.WebTechMidExam.domain.Doctor;
import onehealthline.WebTechMidExam.domain.Specialty;
import onehealthline.WebTechMidExam.repo.ClinicRepository;
import onehealthline.WebTechMidExam.repo.DoctorRepository;
import onehealthline.WebTechMidExam.repo.SpecialtyRepository;
import onehealthline.WebTechMidExam.web.dto.CreateDoctorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	private final DoctorRepository doctorRepository;
	private final ClinicRepository clinicRepository;
	private final SpecialtyRepository specialtyRepository;

	public DoctorController(
			DoctorRepository doctorRepository,
			ClinicRepository clinicRepository,
			SpecialtyRepository specialtyRepository
	) {
		this.doctorRepository = doctorRepository;
		this.clinicRepository = clinicRepository;
		this.specialtyRepository = specialtyRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Doctor create(@Valid @RequestBody CreateDoctorRequest req) {
		Clinic clinic = clinicRepository.findById(req.clinicId())
				.orElseThrow(() -> new IllegalArgumentException("Clinic not found: " + req.clinicId()));

		Set<Specialty> specialties = new HashSet<>(specialtyRepository.findAllById(req.specialtyIds()));
		if (specialties.size() != req.specialtyIds().size()) {
			throw new IllegalArgumentException("One or more specialties not found");
		}

		Doctor doctor = new Doctor();
		doctor.setFullName(req.fullName());
		doctor.setEmail(req.email());
		doctor.setClinic(clinic);
		doctor.setSpecialties(specialties);

		return doctorRepository.save(doctor);
	}

	@GetMapping
	public Page<Doctor> list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "fullName") String sortBy,
			@RequestParam(defaultValue = "asc") String direction
	) {
		Sort sort = "desc".equalsIgnoreCase(direction)
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		return doctorRepository.findAll(pageable);
	}
}

