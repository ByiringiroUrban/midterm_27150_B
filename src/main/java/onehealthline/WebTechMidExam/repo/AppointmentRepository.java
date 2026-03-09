package onehealthline.WebTechMidExam.repo;

import onehealthline.WebTechMidExam.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatient_Id(Long patientId);

	List<Appointment> findByDoctor_Id(Long doctorId);
}

