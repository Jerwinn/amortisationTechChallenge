package dev.jerwin.amortisation.controllers;

import dev.jerwin.amortisation.models.AmortisationDetails;
import dev.jerwin.amortisation.models.LoanDetails;
import dev.jerwin.amortisation.repositories.AmortisationScheduleRepository;
import dev.jerwin.amortisation.repositories.LoanDetailsRepository;
import dev.jerwin.amortisation.services.AmortisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amortisations")
public class AmortisationController {

    @Autowired
    private AmortisationService amortizationService;

    @Autowired
    private AmortisationScheduleRepository repository;

    @Autowired
    private LoanDetailsRepository loanDetailsRepository;

    @PostMapping
    public AmortisationDetails createSchedule(@RequestBody LoanDetails details) {
        LoanDetails savedDetails = loanDetailsRepository.save(details);
        AmortisationDetails schedule = amortizationService.generateSchedule(savedDetails);
        return repository.save(schedule);
    }

    @GetMapping
    public List<AmortisationDetails> getAllSchedules() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public AmortisationDetails getScheduleById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

}
