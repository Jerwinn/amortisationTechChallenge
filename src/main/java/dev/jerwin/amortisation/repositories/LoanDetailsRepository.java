package dev.jerwin.amortisation.repositories;

import dev.jerwin.amortisation.models.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
}