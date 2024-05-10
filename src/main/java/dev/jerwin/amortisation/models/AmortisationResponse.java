package dev.jerwin.amortisation.models;

import java.math.BigDecimal;
import java.util.List;

public record AmortisationResponse(
        Long id,
        BigDecimal loanAmount,
        BigDecimal deposit,
        BigDecimal interestRate,
        int numberOfPayments,
        BigDecimal balloonPayments,
        BigDecimal monthlyPayments,
        BigDecimal totalInterest,
        BigDecimal totalPayments,
        List<AmortisationEntry> amortisationEntryList
) {
}
