package com.ledger.co.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoanDetails {

    private String bankName;

    private String borrowerName;

    private double paidAmount;

    private long remainingInstallmentCount;
}
