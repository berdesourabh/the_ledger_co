package com.ledger.co.commands;

import com.ledger.co.model.Command;
import com.ledger.co.service.BankingOperationService;

public class PaymentCommandExecutor extends CommandExecutor {

    public static final String PAYMENT_COMMAND_NAME = "PAYMENT";

    public static final Integer BANK_NAME_INDEX = 0;
    public static final Integer BORROWER_NAME_INDEX = 1;
    public static final Integer AMOUNT_INDEX = 2;
    public static final Integer EMI_NUMBER_INDEX = 3;

    //TODO: revisit this declaration in child class than parent.
    private BankingOperationService bankingOperationService;

    public PaymentCommandExecutor(BankingOperationService bankingOperationService) {
        this.bankingOperationService = bankingOperationService;
    }

    @Override
    public boolean validate(Command command) {
        return PAYMENT_COMMAND_NAME.equalsIgnoreCase(command.getCommandName());
    }

    @Override
    public void execute(Command command) {
        String bankName = command.getParams().get(BANK_NAME_INDEX);
        String borrowerName = command.getParams().get(BORROWER_NAME_INDEX);
        double amount = Double.parseDouble(command.getParams().get(AMOUNT_INDEX));
        int emiNumber = Integer.parseInt(command.getParams().get(EMI_NUMBER_INDEX));

        bankingOperationService.payment(bankName, borrowerName, amount, emiNumber);
    }
}
