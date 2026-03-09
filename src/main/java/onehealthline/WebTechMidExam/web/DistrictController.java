package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.District;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.web.dto.CreateDistrictRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {

	private final DistrictRepository districtRepository;
	private final ProvinceRepository provinceRepository;

	public DistrictController(DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
		this.districtRepository = districtRepository;
		this.provinceRepository = provinceRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public District create(@Valid @RequestBody CreateDistrictRequest req) {
		Province province = provinceRepository.findById(req.provinceId())
				.orElseThrow(() -> new IllegalArgumentException("Province not found: " + req.provinceId()));

		District d = new District();
		d.setName(req.name());
		d.setProvince(province);
		return districtRepository.save(d);
	}

	@GetMapping
	public List<District> list() {
		return districtRepository.findAll();
	}
}

