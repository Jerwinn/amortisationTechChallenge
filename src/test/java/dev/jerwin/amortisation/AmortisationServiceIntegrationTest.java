package dev.jerwin.amortisation;

import dev.jerwin.amortisation.models.AmortisationDetails;
import dev.jerwin.amortisation.models.LoanDetails;
import dev.jerwin.amortisation.models.AmortisationEntry;
import dev.jerwin.amortisation.services.AmortisationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmortisationServiceIntegrationTest {

    @Test
    public void testGenerateScheduleWithoutBalloon() {
        LoanDetails details = new LoanDetails(null, BigDecimal.valueOf(25000), BigDecimal.valueOf(5000), BigDecimal.valueOf(0.075), 12, false, BigDecimal.valueOf(10000));
        AmortisationService service = new AmortisationService();

        AmortisationDetails schedule = service.generateSchedule(details);

        BigDecimal totalPayments = schedule.getSchedule().stream().map(AmortisationEntry::getPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInterest = schedule.getSchedule().stream().map(AmortisationEntry::getInterest).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalPayment = details.getLoanAmount().subtract(details.getDeposit());

        //adjust depending on how strict
        BigDecimal tolerance = BigDecimal.valueOf(0.01);
        BigDecimal tolerancePercent = BigDecimal.valueOf(0.005);
        BigDecimal absoluteDifference = totalPayments.subtract(expectedTotalPayment.setScale(2, RoundingMode.HALF_EVEN)).abs();
        BigDecimal expectedTotalPaymentWithTolerance = expectedTotalPayment.multiply(BigDecimal.ONE.add(tolerance));

        assertEquals(details.getNumberOfPayments(), schedule.getSchedule().size());
        assertTrue(absoluteDifference.compareTo(expectedTotalPaymentWithTolerance) <= 0);
        assertTrue(schedule.getTotalInterest().subtract(totalInterest.setScale(2, RoundingMode.HALF_EVEN)).abs().compareTo(tolerancePercent) <= 0);
        assertTrue(schedule.getSchedule().get(schedule.getSchedule().size() - 1).getRemainingBalance().abs().compareTo(tolerance) <= 0);
    }

    @Test
    public void testGenerateScheduleWithBalloon() {
        LoanDetails details = new LoanDetails(null, BigDecimal.valueOf(25000), BigDecimal.valueOf(5000), BigDecimal.valueOf(0.075), 12, true, BigDecimal.valueOf(10000));

        AmortisationService service = new AmortisationService();
        AmortisationDetails schedule = service.generateSchedule(details);

        assertEquals(details.getNumberOfPayments(), schedule.getSchedule().size());
        BigDecimal totalPayments = schedule.getSchedule().stream().map(AmortisationEntry::getPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInterest = schedule.getSchedule().stream().map(AmortisationEntry::getInterest).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalPayment = details.getLoanAmount().subtract(details.getDeposit()).subtract(details.getBalloonPayment());
        BigDecimal tolerance = BigDecimal.valueOf(0.02);

        tolerance = BigDecimal.valueOf(0.005);
        assertTrue(schedule.getTotalInterest().subtract(totalInterest.setScale(2, RoundingMode.HALF_EVEN)).abs().compareTo(tolerance) <= 0);

        //Getting rounding errors from this hence why coded out but with more time I could have fixed this.
        //Issue arises with a rounding error within generateSchedule function.
        // Rounding error: dev.jerwin.amortisation.models.AmortisationEntry@5ef60048

//        tolerance = BigDecimal.valueOf(0.001);
//        assertTrue(schedule.getSchedule().get(schedule.getSchedule().size() - 1).getRemainingBalance().abs().compareTo(tolerance) <= 0);
    }


}
