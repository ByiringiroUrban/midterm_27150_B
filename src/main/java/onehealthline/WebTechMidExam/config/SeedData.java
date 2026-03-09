package onehealthline.WebTechMidExam.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import onehealthline.WebTechMidExam.domain.District;
import onehealthline.WebTechMidExam.domain.Province;
import onehealthline.WebTechMidExam.domain.Specialty;
import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.repo.SpecialtyRepository;

@Configuration
public class SeedData {

	/**
	 * Seed data was used during development.
	 *
	 * For the final exam demo we want to start with
	 * completely empty tables so that we can show
	 * how records are created only through the APIs.
	 *
	 * This runner now does nothing.
	 */
	@Bean
	CommandLineRunner seed(
			ProvinceRepository provinceRepository,
			DistrictRepository districtRepository,
			SpecialtyRepository specialtyRepository
	) {
		return args -> {
			// no-op: keep tables empty on startup
		};
	}
}

