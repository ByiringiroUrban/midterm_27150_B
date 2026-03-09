package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.Specialty;
import onehealthline.WebTechMidExam.repo.SpecialtyRepository;
import onehealthline.WebTechMidExam.web.dto.CreateSpecialtyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

	private final SpecialtyRepository specialtyRepository;

	public SpecialtyController(SpecialtyRepository specialtyRepository) {
		this.specialtyRepository = specialtyRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Specialty create(@Valid @RequestBody CreateSpecialtyRequest req) {
		Specialty s = new Specialty();
		s.setName(req.name());
		return specialtyRepository.save(s);
	}

	@GetMapping
	public List<Specialty> list() {
		return specialtyRepository.findAll();
	}
}

