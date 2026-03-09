package onehealthline.WebTechMidExam.repo;

import onehealthline.WebTechMidExam.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long> {

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByPhone(String phone);

	List<AppUser> findByProvince_CodeIgnoreCase(String code);

	List<AppUser> findByProvince_NameIgnoreCase(String name);
}

