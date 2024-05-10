package dev.jerwin.amortisation.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class LoanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal loanAmount;
    private BigDecimal deposit;
    private BigDecimal interestRate;
    private int numberOfPayments;
    private boolean includeBalloonPayment;
    @Nullable
    private BigDecimal balloonPayment;


    public LoanDetails(Long id, BigDecimal loanAmount, BigDecimal deposit, BigDecimal interestRate, int numberOfPayments, boolean includeBalloonPayment, @Nullable BigDecimal balloonPayment) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.deposit = deposit;
        this.interestRate = interestRate;
        this.numberOfPayments = numberOfPayments;
        this.includeBalloonPayment = includeBalloonPayment;
        this.balloonPayment = balloonPayment;
    }


    public LoanDetails() {
        this.id = null;
        this.loanAmount = BigDecimal.ZERO;
        this.deposit = BigDecimal.ZERO;
        this.interestRate = BigDecimal.ZERO;
        this.numberOfPayments = 0;
        this.includeBalloonPayment = false;
        this.balloonPayment = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public BigDecimal getBalloonPayment() {
        return balloonPayment;
    }

    public void setBalloonPayment(BigDecimal balloonPayment) {
        this.balloonPayment = balloonPayment;
    }

    public boolean isIncludeBalloonPayment() {
        return includeBalloonPayment;
    }

    public void setIncludeBalloonPayment(boolean includeBalloonPayment) {
        this.includeBalloonPayment = includeBalloonPayment;
    }
}
