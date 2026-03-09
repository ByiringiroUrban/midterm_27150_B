package onehealthline.WebTechMidExam.repo;

import onehealthline.WebTechMidExam.domain.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {

	Optional<PatientProfile> findByUser_Id(Long userId);
}

