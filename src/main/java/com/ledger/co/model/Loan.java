package com.ledger.co.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Loan {

    private String borrowerName;

    private double principalAmount;

    private short tenure;

    private float rateOfInterest;

    private double totalAmountToPay;

    private double totalPaidAmount;

    private List<Installment> installmentList;

    private String status;
}
