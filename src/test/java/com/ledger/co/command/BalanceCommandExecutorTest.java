package com.ledger.co.command;

import com.ledger.co.commands.BalanceCommandExecutor;
import com.ledger.co.commands.LoanCommandExecutor;
import com.ledger.co.model.BankingSystem;
import com.ledger.co.model.Command;
import com.ledger.co.service.BankingOperationService;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BalanceCommandExecutorTest {

    private final BankingSystem bankingSystemInstance = BankingSystem.getBankingSystemInstance();
    private final BankingOperationService bankingOperationService = new BankingOperationService(bankingSystemInstance);

    private final LoanCommandExecutor loanCommandExecutor = new LoanCommandExecutor(bankingOperationService);
    private final BalanceCommandExecutor balanceCommandExecutor = new BalanceCommandExecutor(bankingOperationService);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        bankingSystemInstance.getBankLoans().clear();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

   @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testValidateWithValidCommandName() {
        boolean validCommand = balanceCommandExecutor.validate(new Command("BALANCE BANK_NAME BORROWER_NAME 2"));
        Assertions.assertTrue(validCommand);
    }

    @Test
    void testValidateWithInvalidCommandName() {
        boolean invalidCommand = balanceCommandExecutor.validate(new Command("INVALID BANK_NAME BORROWER_NAME 2"));
        Assertions.assertFalse(invalidCommand);
    }

    @Test
    void testExecuteSuccess() {
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME BORROWER_NAME 10000 5 4"));
        balanceCommandExecutor.execute(new Command("BALANCE BANK_NAME BORROWER_NAME 2"));
        Assertions.assertEquals("BANK_NAME BORROWER_NAME 0.0 60\n", outContent.toString());
    }

    @Test
    void testExecuteLoanDoesNotExists() {
        balanceCommandExecutor.execute(new Command("BALANCE BANK_NAME BORROWER_NAME 2"));
        Assertions.assertEquals("", outContent.toString());
    }
}
