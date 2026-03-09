package onehealthline.WebTechMidExam.repo;

import onehealthline.WebTechMidExam.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

