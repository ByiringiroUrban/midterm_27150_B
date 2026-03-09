package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.AppUser;
import onehealthline.WebTechMidExam.domain.District;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.repo.UserRepository;
import onehealthline.WebTechMidExam.web.dto.CreateUserRequest;
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
@RequestMapping("/api/users")
public class UserController {

	private final UserRepository userRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;

	public UserController(
			UserRepository userRepository,
			ProvinceRepository provinceRepository,
			DistrictRepository districtRepository
	) {
		this.userRepository = userRepository;
		this.provinceRepository = provinceRepository;
		this.districtRepository = districtRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AppUser create(@Valid @RequestBody CreateUserRequest req) {
		if (userRepository.existsByEmailIgnoreCase(req.email())) {
			throw new IllegalArgumentException("Email already exists: " + req.email());
		}
		if (userRepository.existsByPhone(req.phone())) {
			throw new IllegalArgumentException("Phone already exists: " + req.phone());
		}

		Province province = provinceRepository.findById(req.provinceId())
				.orElseThrow(() -> new IllegalArgumentException("Province not found: " + req.provinceId()));
		District district = districtRepository.findById(req.districtId())
				.orElseThrow(() -> new IllegalArgumentException("District not found: " + req.districtId()));

		AppUser user = new AppUser();
		user.setFullName(req.fullName());
		user.setEmail(req.email());
		user.setPhone(req.phone());
		user.setProvince(province);
		user.setDistrict(district);

		return userRepository.save(user);
	}

	@GetMapping
	public List<AppUser> list() {
		return userRepository.findAll();
	}

	@GetMapping("/by-province")
	public List<AppUser> getUsersByProvince(
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String name
	) {
		if (code != null && !code.isBlank()) {
			return userRepository.findByProvince_CodeIgnoreCase(code.trim());
		}
		if (name != null && !name.isBlank()) {
			return userRepository.findByProvince_NameIgnoreCase(name.trim());
		}
		throw new IllegalArgumentException("Provide province 'code' OR 'name'");
	}
}

