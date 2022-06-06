package com.ledger.co.service;

import com.ledger.co.dto.LoanDetails;
import com.ledger.co.model.BankingSystem;
import com.ledger.co.model.Loan;

import java.util.Optional;

public class BankingOperationService {

    private final BankingSystem bankingSystem;

    public BankingOperationService(BankingSystem bankingSystem) {
        this.bankingSystem = bankingSystem;
    }

    public void createLoan(String bankName, Loan loan) {
        bankingSystem.createLoan(bankName, loan);
    }

    public Optional<LoanDetails> getLoanDetails(String bankName, String borrowerName, int emiNumber) {
        return bankingSystem.getLoanDetails(bankName,borrowerName,emiNumber);
    }

    public void payment(String bankName, String borrowerName, double amount, int emiNumber) {
        bankingSystem.payment(bankName,borrowerName,amount,emiNumber);
    }

}
