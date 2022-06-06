package com.ledger.co.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Installment {

    private int number;

    private double amount;

    private boolean isPaid;
}
