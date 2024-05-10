package dev.jerwin.amortisation.services;

import dev.jerwin.amortisation.models.AmortisationEntry;
import dev.jerwin.amortisation.models.AmortisationDetails;
import dev.jerwin.amortisation.models.LoanDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.math.MathContext.DECIMAL128;

@Service
public class AmortisationService {

    public AmortisationDetails generateSchedule(LoanDetails details) {
        AmortisationDetails schedule = new AmortisationDetails();
        schedule.setLoanDetails(details);

        BigDecimal loanAmount = details.getLoanAmount().subtract(details.getDeposit());
        BigDecimal interestRatePerPeriod = details.getInterestRate().divide(BigDecimal.valueOf(12));
        int numberOfPayments = details.getNumberOfPayments();

        // Determine monthly payment based on whether they have a balloon payment or not
        BigDecimal monthlyPayment = details.isIncludeBalloonPayment()
                ? calculateMonthlyPaymentWithBalloon(loanAmount, details.getBalloonPayment(), interestRatePerPeriod, numberOfPayments)
                : calculateMonthlyPayment(loanAmount, interestRatePerPeriod, numberOfPayments);

        schedule.setMonthlyPayment(monthlyPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN));

        // building the amortization schedule
        List<AmortisationEntry> entries = buildSchedule(details);
        schedule.setSchedule(entries);

        BigDecimal totalInterest = entries.stream().map(AmortisationEntry::getInterest).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(entries.size()));
        BigDecimal roundedTotalInterest = totalInterest.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal roundedTotalPayments = totalPayments.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        schedule.setTotalPayments(roundedTotalPayments);
        schedule.setTotalInterest(roundedTotalInterest);

        return schedule;
    }


    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal interestRate, int numberOfPayments) {
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive.");
        }

        // exponent
        BigDecimal exponent = BigDecimal.ONE.add(interestRate).pow(numberOfPayments);

        // P * ((r * (1 + r) ^ n) / ((1 + r) ^ n - 1))
        // n is exponent calculated above
        BigDecimal monthlyPayments = loanAmount.multiply(interestRate.multiply(exponent)).divide(exponent.subtract(BigDecimal.ONE), DECIMAL128);

        return monthlyPayments;
    }


    public BigDecimal calculateMonthlyPaymentWithBalloon(BigDecimal loanAmount, BigDecimal balloonPayment, BigDecimal interestRate, int numberOfPayments) {
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive.");
        }

        // P * ((r * (1 + r) ^ n) / ((1 + r) ^ n - 1))
        // i split into the half so each side gets calculated first then the multiply
        // kind of made it easier to get my head wrapped around the equation
        int absoluteNumberOfPayments = Math.abs(numberOfPayments);
        BigDecimal onePlusInterestRaised = BigDecimal.ONE.add(interestRate).pow(absoluteNumberOfPayments);
        BigDecimal presentValueOfBalloon = balloonPayment.divide(onePlusInterestRaised, RoundingMode.HALF_EVEN).setScale(5, RoundingMode.HALF_EVEN);
        BigDecimal topDivisor = loanAmount.subtract(presentValueOfBalloon);

        BigDecimal innerPart = BigDecimal.ONE.subtract(onePlusInterestRaised.pow(-1, DECIMAL128)).setScale(5, RoundingMode.HALF_EVEN);
        BigDecimal bottomDivisor = interestRate.divide(innerPart, RoundingMode.HALF_EVEN).setScale(5, RoundingMode.HALF_EVEN);

        BigDecimal monthlyPayment = topDivisor.multiply(bottomDivisor).setScale(5, RoundingMode.HALF_EVEN);

        return monthlyPayment;
    }



    private List<AmortisationEntry> buildSchedule(LoanDetails details) {
        List<AmortisationEntry> entries = new ArrayList<>();

        BigDecimal loanAmount = details.getLoanAmount().subtract(details.getDeposit());
        BigDecimal interestRate = details.getInterestRate().divide(BigDecimal.valueOf(12));
        int numberOfPayments = details.getNumberOfPayments();


        BigDecimal monthlyPayment = details.isIncludeBalloonPayment()
                ? calculateMonthlyPaymentWithBalloon(loanAmount, details.getBalloonPayment(), interestRate, numberOfPayments)
                : calculateMonthlyPayment(loanAmount, interestRate, numberOfPayments);

        BigDecimal remainingBalance = loanAmount;
        BigDecimal roundingError = BigDecimal.ZERO;

        for (int i = 1; i <= numberOfPayments; i++) {
            BigDecimal interest = remainingBalance.multiply(interestRate);
            BigDecimal principal = monthlyPayment.subtract(interest);


            BigDecimal paymentPlusInterest = principal.add(interest);

            remainingBalance = remainingBalance.subtract(principal);
            roundingError = roundingError.add(paymentPlusInterest.subtract(monthlyPayment));
            principal = principal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            interest = interest.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            remainingBalance = remainingBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            monthlyPayment = monthlyPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN);

            if (i == numberOfPayments) {
                principal = principal.add(roundingError).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                if (details.isIncludeBalloonPayment()) {
                    remainingBalance = remainingBalance.setScale(2, BigDecimal.ROUND_DOWN);

                } else {
                    monthlyPayment = monthlyPayment.add(remainingBalance);
                    remainingBalance = BigDecimal.ZERO;
                }
            }

            AmortisationEntry entry = new AmortisationEntry(null, i, monthlyPayment, principal, interest, remainingBalance);

            entries.add(entry);
        }

        return entries;
    }

}
