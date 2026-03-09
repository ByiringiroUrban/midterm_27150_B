package onehealthline.WebTechMidExam.repo;

import onehealthline.WebTechMidExam.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
	Optional<Province> findByCodeIgnoreCase(String code);

	Optional<Province> findByNameIgnoreCase(String name);
}

