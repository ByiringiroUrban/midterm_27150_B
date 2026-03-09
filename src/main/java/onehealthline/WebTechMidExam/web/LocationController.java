package onehealthline.WebTechMidExam.web;

import jakarta.validation.Valid;
import onehealthline.WebTechMidExam.domain.District;
import onehealthline.WebTechMidExam.domain.Location;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.LocationRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.web.dto.CreateLocationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

	private final LocationRepository locationRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;

	public LocationController(
			LocationRepository locationRepository,
			ProvinceRepository provinceRepository,
			DistrictRepository districtRepository
	) {
		this.locationRepository = locationRepository;
		this.provinceRepository = provinceRepository;
		this.districtRepository = districtRepository;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Location create(@Valid @RequestBody CreateLocationRequest req) {
		Province province = provinceRepository.findById(req.provinceId())
				.orElseThrow(() -> new IllegalArgumentException("Province not found: " + req.provinceId()));
		District district = districtRepository.findById(req.districtId())
				.orElseThrow(() -> new IllegalArgumentException("District not found: " + req.districtId()));

		Location location = new Location();
		location.setSector(req.sector());
		location.setCell(req.cell());
		location.setVillage(req.village());
		location.setProvince(province);
		location.setDistrict(district);

		return locationRepository.save(location);
	}
}

