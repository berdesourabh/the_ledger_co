package com.ledger.co.util;

import com.ledger.co.model.Installment;
import com.ledger.co.model.Loan;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModelMapperUtil {

    public static final Integer BORROWER_NAME_INDEX = 1;
    public static final Integer PRINCIPAL_AMOUNT_INDEX = 2;
    public static final Integer NO_OF_YEARS_INDEX = 3;
    public static final Integer RATE_OF_INTEREST_INDEX = 4;

    public static Loan mapToLoan(List<String> params) {

        Loan loan = Loan.builder().borrowerName(params.get(BORROWER_NAME_INDEX))
                .principalAmount(Double.parseDouble(params.get(PRINCIPAL_AMOUNT_INDEX)))
                .tenure(Short.parseShort(params.get(NO_OF_YEARS_INDEX)))
                .rateOfInterest(Float.parseFloat(params.get(RATE_OF_INTEREST_INDEX))).status("ONGOING").build();
        setInstallments(loan);
        return loan;
    }

    private static void setInstallments(Loan loan) {
        short tenure = loan.getTenure();
        double interestAmount = Math.ceil(calculateInterest(loan.getPrincipalAmount(), tenure, loan.getRateOfInterest()));
        double totalAmountPayable = loan.getPrincipalAmount() + interestAmount;
        loan.setTotalAmountToPay(totalAmountPayable);
        double monthlyEmi = Math.ceil(totalAmountPayable / (12 * tenure));
        List<Installment> installments = IntStream.iterate(1, i -> i + 1)
                .mapToObj(i -> Installment.builder().number(i).amount(monthlyEmi).build())
                .limit(tenure * 12)
                .collect(Collectors.toList());
        loan.setInstallmentList(installments);
    }

    private static double calculateInterest(double principalAmount, short tenure, float rateOfInterest) {
        return principalAmount * tenure * (rateOfInterest / 100);
    }
}
