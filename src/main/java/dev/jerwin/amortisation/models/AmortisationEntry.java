package dev.jerwin.amortisation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class AmortisationEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int period;
    private BigDecimal payment;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal remainingBalance;
//    private LocalDateTime date;
//    private double interestRate;


    public AmortisationEntry(Long id, int period, BigDecimal payment, BigDecimal principal, BigDecimal interest, BigDecimal remainingBalance) {
        this.id = id;
        this.period = period;
        this.payment = payment;
        this.principal = principal;
        this.interest = interest;
        this.remainingBalance = remainingBalance;
    }

    public AmortisationEntry() {
        this.id = null;
        this.period = 0;
        this.payment = BigDecimal.ZERO;
        this.principal = BigDecimal.ZERO;
        this.interest = BigDecimal.ZERO;
        this.remainingBalance = BigDecimal.ZERO;
    }


    public int getPeriod() {
        return period;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
