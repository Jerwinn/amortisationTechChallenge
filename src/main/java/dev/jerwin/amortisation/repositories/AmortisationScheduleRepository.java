package dev.jerwin.amortisation.repositories;

import dev.jerwin.amortisation.models.AmortisationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmortisationScheduleRepository extends JpaRepository<AmortisationDetails, Long> {
}