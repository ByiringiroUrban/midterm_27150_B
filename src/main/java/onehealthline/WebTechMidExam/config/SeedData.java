package onehealthline.WebTechMidExam.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import onehealthline.WebTechMidExam.repo.DistrictRepository;
import onehealthline.WebTechMidExam.repo.ProvinceRepository;
import onehealthline.WebTechMidExam.repo.SpecialtyRepository;

@Configuration
public class SeedData {

	/** * Seeds the database with initial data on application startup.
	 */
	@Bean
	CommandLineRunner seed(
			ProvinceRepository provinceRepository,
			DistrictRepository districtRepository,
			SpecialtyRepository specialtyRepository
	) {
		return args -> {
			
		};
	}
}

