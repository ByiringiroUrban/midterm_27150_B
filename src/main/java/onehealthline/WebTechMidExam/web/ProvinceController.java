package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.web.dto.CreateProvinceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

	private final ProvinceRepository provinceRepository;

	public ProvinceController(ProvinceRepository provinceRepository) {
		this.provinceRepository = provinceRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Province create(@Valid @RequestBody CreateProvinceRequest req) {
		Province p = new Province();
		p.setCode(req.code());
		p.setName(req.name());
		return provinceRepository.save(p);
	}

	@GetMapping
	public List<Province> list() {
		return provinceRepository.findAll();
	}
}

