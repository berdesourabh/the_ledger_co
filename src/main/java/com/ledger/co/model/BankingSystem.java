package com.ledger.co.model;

import com.ledger.co.dto.LoanDetails;
import com.ledger.co.exception.BorrowerHasNoLoanException;
import com.ledger.co.exception.LoanAlreadyExistsException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class BankingSystem {

    public static BankingSystem bankingSystem = null;

    private Map<String, List<Loan>> bankLoans;

    private BankingSystem() {
        bankLoans = new HashMap<>();
    }

    public static BankingSystem getBankingSystemInstance() {
        if (bankingSystem == null) {
            bankingSystem = new BankingSystem();
        }
        return bankingSystem;
    }

    public void createLoan(String bankName, Loan loan) {
        if (bankLoans.containsKey(bankName)) {
            List<Loan> loans = bankLoans.get(bankName);
            boolean existingOngoingLoan = isExistingOngoingLoan(loan, loans);
            if (existingOngoingLoan) {
                throw new LoanAlreadyExistsException();
            }
            loans.add(loan);
        } else {
            bankLoans.put(bankName.toUpperCase(), Collections.singletonList(loan));
        }
    }

    public Optional<LoanDetails> getLoanDetails(String bankName, String borrowerName, int emiNumber) {
        if (bankLoans.containsKey(bankName)) {
            List<Loan> loans = bankLoans.get(bankName);
            double amount;
            Optional<Loan> borrowerLoan = loans.stream().filter(loan -> loan.getBorrowerName().equals(borrowerName)).findFirst();
            if (borrowerLoan.isEmpty()) {
                throw new BorrowerHasNoLoanException();
            }
            Loan loan = borrowerLoan.get();
            amount = loan.getInstallmentList().stream()
                    //.filter(Installment::isPaid)
                    .limit(emiNumber)
                    .map(Installment::getAmount)
                    .reduce((double) 0, Double::sum);
//            long remainingInstallments = loan.getInstallmentList().stream().filter(installment -> !installment.isPaid()).count();
            long remainingInstallments = loan.getInstallmentList().size() - emiNumber;
            return Optional.of(LoanDetails.builder().bankName(bankName).borrowerName(borrowerName).paidAmount(amount).remainingInstallmentCount(remainingInstallments).build());
        }
        return Optional.empty();
    }

    public void payment(String bankName, String borrowerName, double amount, int emiNumber) {
        if (bankLoans.containsKey(bankName)) {
            List<Loan> loans = bankLoans.get(bankName);
            Optional<Loan> borrowerLoan = loans.stream()
                    .filter(loan -> loan.getBorrowerName().equals(borrowerName) && loan.getStatus().equals("ONGOING"))
                    .findFirst();
            borrowerLoan.ifPresent(loan -> updateInstallments(amount, emiNumber, loan));
        }
    }

    private void updateInstallments(double amount, int emiNumber, Loan borrowerLoan) {
        List<Installment> installmentList = borrowerLoan.getInstallmentList();
        installmentList.stream()
                .limit(emiNumber)
                .forEach(i -> {
                    i.setPaid(true);
                    borrowerLoan.setTotalPaidAmount(borrowerLoan.getTotalPaidAmount() + i.getAmount());
                });
        Optional<Installment> nextInstallment = installmentList.stream().filter(i -> i.getNumber() == emiNumber + 1).findFirst();
        nextInstallment.ifPresent(installment -> {
            if (amount > installment.getAmount()) {
                double monthlyEmiAmount = installment.getAmount();
                installment.setAmount(amount);
                installment.setPaid(true);
                borrowerLoan.setTotalPaidAmount(borrowerLoan.getTotalPaidAmount() + installment.getAmount());
                recalculateEMI(borrowerLoan, monthlyEmiAmount, emiNumber + 1);
            }
        });
    }

    private void recalculateEMI(Loan borrowerLoan, double monthlyEmiAmount, int emiNumber) {
        borrowerLoan.getInstallmentList().subList(emiNumber, borrowerLoan.getInstallmentList().size()).clear();
        double newTotalAmountToPay = borrowerLoan.getTotalAmountToPay() - borrowerLoan.getTotalPaidAmount();
        long newEmiTenure = (long) Math.ceil(newTotalAmountToPay / monthlyEmiAmount);

        List<Installment> reCalculatedInstallments = IntStream.iterate(emiNumber + 1, i -> i + 1)
                .mapToObj(i -> Installment.builder().number(i).amount(monthlyEmiAmount).build())
                .limit(newEmiTenure - 1)
                .collect(Collectors.toList());
        borrowerLoan.getInstallmentList().addAll(reCalculatedInstallments);
        Installment lastInstallment = Installment.builder()
                .number(borrowerLoan.getInstallmentList().size() + 1)
                .amount(newTotalAmountToPay - (monthlyEmiAmount * (newEmiTenure - 1)))
                .build();
        borrowerLoan.getInstallmentList().add(lastInstallment);
        borrowerLoan.setTenure((short) borrowerLoan.getInstallmentList().size());
    }
    private boolean isExistingOngoingLoan(Loan loan, List<Loan> loans) {
        return loans.stream().anyMatch(l -> l.getBorrowerName().equals(loan.getBorrowerName()) && l.getStatus().equals("ONGOING"));
    }
}