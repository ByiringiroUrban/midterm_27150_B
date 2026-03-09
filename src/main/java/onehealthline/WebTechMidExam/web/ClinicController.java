package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.Clinic;
import onehealthline.WebTechMidExam.domain.District;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.repo.ClinicRepository;
import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.web.dto.CreateClinicRequest;
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

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

	private final ClinicRepository clinicRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;

	public ClinicController(
			ClinicRepository clinicRepository,
			ProvinceRepository provinceRepository,
			DistrictRepository districtRepository
	) {
		this.clinicRepository = clinicRepository;
		this.provinceRepository = provinceRepository;
		this.districtRepository = districtRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Clinic create(@Valid @RequestBody CreateClinicRequest req) {
		Province province = provinceRepository.findById(req.provinceId())
				.orElseThrow(() -> new IllegalArgumentException("Province not found: " + req.provinceId()));
		District district = districtRepository.findById(req.districtId())
				.orElseThrow(() -> new IllegalArgumentException("District not found: " + req.districtId()));

		Clinic clinic = new Clinic();
		clinic.setName(req.name());
		clinic.setProvince(province);
		clinic.setDistrict(district);

		return clinicRepository.save(clinic);
	}

	@GetMapping
	public List<Clinic> list() {
		return clinicRepository.findAll();
	}

	@GetMapping("/page")
	public Page<Clinic> listPaginated(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction
	) {
		Sort sort = "desc".equalsIgnoreCase(direction)
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return clinicRepository.findAll(pageable);
	}
}

