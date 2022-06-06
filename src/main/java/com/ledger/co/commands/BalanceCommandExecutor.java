package com.ledger.co.commands;

import com.ledger.co.dto.LoanDetails;
import com.ledger.co.model.Command;
import com.ledger.co.service.BankingOperationService;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;

public class BalanceCommandExecutor extends CommandExecutor {

    public static final String BALANCE_COMMAND_NAME = "BALANCE";

    public static final Integer BANK_NAME_INDEX = 0;
    public static final Integer BORROWER_NAME_INDEX = 1;
    public static final Integer EMI_NUMBER_INDEX = 2;

    private DecimalFormat decimalFormat = new DecimalFormat("#");

    private final BankingOperationService bankingOperationService;

    public BalanceCommandExecutor(BankingOperationService bankingOperationService) {
        this.bankingOperationService = bankingOperationService;
    }

    @Override
    public boolean validate(Command command) {
        return BALANCE_COMMAND_NAME.equalsIgnoreCase(command.getCommandName());
    }

    @Override
    public void execute(Command command) {
        String bankName = command.getParams().get(BANK_NAME_INDEX);
        String borrowerName = command.getParams().get(BORROWER_NAME_INDEX);
        int emiNumber = Integer.parseInt(command.getParams().get(EMI_NUMBER_INDEX));
        if(Objects.isNull(bankName) || Objects.isNull(borrowerName)) {
            return;
        }

        Optional<LoanDetails> loanDetails = bankingOperationService.getLoanDetails(bankName, borrowerName, emiNumber);

        loanDetails.ifPresent(details -> System.out.println(details.getBankName() + " " + details.getBorrowerName() + " " + decimalFormat.format(Math.round(details.getPaidAmount())) + " " + details.getRemainingInstallmentCount()));
    }
}
