package com.ledger.co.command;

import com.ledger.co.commands.BalanceCommandExecutor;
import com.ledger.co.commands.LoanCommandExecutor;
import com.ledger.co.commands.PaymentCommandExecutor;
import com.ledger.co.model.BankingSystem;
import com.ledger.co.model.Command;
import com.ledger.co.service.BankingOperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentCommandExecutorTest {


    private final BankingSystem bankingSystemInstance = BankingSystem.getBankingSystemInstance();
    private final BankingOperationService bankingOperationService = new BankingOperationService(bankingSystemInstance);

    private final LoanCommandExecutor loanCommandExecutor = new LoanCommandExecutor(bankingOperationService);
    private final PaymentCommandExecutor paymentCommandExecutor = new PaymentCommandExecutor(bankingOperationService);
    private final BalanceCommandExecutor balanceCommandExecutor = new BalanceCommandExecutor(bankingOperationService);


    @Test
    void testValidateWithValidCommandName() {
        boolean validCommand = paymentCommandExecutor.validate(new Command("PAYMENT BANK_NAME BORROWER_NAME 200 1"));
        Assertions.assertTrue(validCommand);
    }

    @Test
    void testValidateWithInvalidCommandName() {
        boolean invalidCommand = paymentCommandExecutor.validate(new Command("INVALID BANK_NAME BORROWER_NAME 2000 1"));
        Assertions.assertFalse(invalidCommand);
    }

    @Test
    void testExecuteSuccess() {
        loanCommandExecutor.execute(new Command("LOAN IDIDI Dale 5000 1 6"));
        paymentCommandExecutor.execute(new Command("PAYMENT IDIDI Dale 1000 5"));
        balanceCommandExecutor.execute(new Command("BALANCE IDIDI Dale 3"));
        balanceCommandExecutor.execute(new Command("BALANCE IDIDI Dale 6"));
    }
}
