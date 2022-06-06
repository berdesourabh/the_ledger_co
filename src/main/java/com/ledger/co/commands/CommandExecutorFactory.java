package com.ledger.co.commands;

import com.ledger.co.service.BankingOperationService;
import com.ledger.co.exception.InvalidCommandException;
import com.ledger.co.model.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class CommandExecutorFactory {
    private final Logger LOGGER = Logger.getLogger("CommandExecutorFactory");
    private final Map<String, CommandExecutor> commandExecutors = new HashMap<>();

    public CommandExecutorFactory(BankingOperationService bankingOperationService) {
        commandExecutors.put(LoanCommandExecutor.LOAN_COMMAND_NAME, new LoanCommandExecutor(bankingOperationService));
        commandExecutors.put(PaymentCommandExecutor.PAYMENT_COMMAND_NAME, new PaymentCommandExecutor(bankingOperationService));
        commandExecutors.put(BalanceCommandExecutor.BALANCE_COMMAND_NAME, new BalanceCommandExecutor(bankingOperationService));
    }

    public CommandExecutor getCommandExecutor(Command command) {
        final CommandExecutor commandExecutor = commandExecutors.get(command.getCommandName());
        if(Objects.isNull(commandExecutor)) {
            throw new InvalidCommandException();
        }
        return commandExecutor;
    }
}
