package com.ledger.co.commands;

import com.ledger.co.model.Command;
import com.ledger.co.model.Loan;
import com.ledger.co.service.BankingOperationService;
import com.ledger.co.util.ModelMapperUtil;

public class LoanCommandExecutor extends CommandExecutor {

    public static final String LOAN_COMMAND_NAME = "LOAN";
    public static final int BANK_NAME_INDEX = 0;

    private final BankingOperationService bankingOperationService;

    public LoanCommandExecutor(BankingOperationService bankingOperationService) {
        this.bankingOperationService = bankingOperationService;
    }

    @Override
    public boolean validate(Command command) {
        return LOAN_COMMAND_NAME.equalsIgnoreCase(command.getCommandName());
    }

    @Override
    public void execute(Command command) {
        Loan loan = ModelMapperUtil.mapToLoan(command.getParams());
        bankingOperationService.createLoan(command.getParams().get(BANK_NAME_INDEX), loan);

    }
}
