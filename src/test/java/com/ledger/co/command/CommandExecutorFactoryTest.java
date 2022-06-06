package com.ledger.co.command;

import com.ledger.co.model.BankingSystem;
import com.ledger.co.service.BankingOperationService;
import com.ledger.co.commands.*;
import com.ledger.co.exception.InvalidCommandException;
import com.ledger.co.model.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandExecutorFactoryTest {

    private CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(new BankingOperationService(BankingSystem.getBankingSystemInstance()));

    @Test
    void testGetCommandExecutorForLoanCommand() {
        Command command = new Command("LOAN BANK_NAME BORROWER_NAME 5000 1 6");
        CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        Assertions.assertTrue(commandExecutor instanceof LoanCommandExecutor);
    }

    @Test
    void testGetCommandExecutorForPaymentCommand() {
        Command command = new Command("PAYMENT BANK_NAME BORROWER_NAME 1000 5");
        CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        Assertions.assertTrue(commandExecutor instanceof PaymentCommandExecutor);
    }

    @Test
    void testGetCommandExecutorForBalanceCommand() {
        Command command = new Command("BALANCE BANK_NAME BORROWER_NAME 3");
        CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        Assertions.assertTrue(commandExecutor instanceof BalanceCommandExecutor);
    }

    @Test
    void testGetCommandExecutorForInvalidCommand() {
        Command command = new Command("INVALID_COMMAND");
        Assertions.assertThrows(InvalidCommandException.class, () ->commandExecutorFactory.getCommandExecutor(command));
    }
}
