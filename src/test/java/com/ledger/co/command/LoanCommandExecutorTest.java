package com.ledger.co.command;

import com.ledger.co.commands.LoanCommandExecutor;
import com.ledger.co.dto.LoanDetails;
import com.ledger.co.exception.LoanAlreadyExistsException;
import com.ledger.co.model.BankingSystem;
import com.ledger.co.model.Command;
import com.ledger.co.service.BankingOperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class LoanCommandExecutorTest {

    private final BankingSystem bankingSystemInstance = BankingSystem.getBankingSystemInstance();
    private final LoanCommandExecutor loanCommandExecutor = new LoanCommandExecutor(new BankingOperationService(bankingSystemInstance));


    @BeforeEach
    void setUp() {
        bankingSystemInstance.getBankLoans().clear();
    }

    @Test
    void testValidateWithValidCommandName() {
        boolean validCommand = loanCommandExecutor.validate(new Command("LOAN BANK_NAME BORROWER_NAME 5000 1 6"));
        Assertions.assertTrue(validCommand);
    }

    @Test
    void testValidateWithInvalidCommandName() {
        boolean invalidCommand = loanCommandExecutor.validate(new Command("INVALID BANK_NAME BORROWER_NAME 5000 1 6"));
        Assertions.assertFalse(invalidCommand);
    }

    @Test
    void testExecuteSuccess() {
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME BORROWER_NAME 10000 5 4"));
        Optional<LoanDetails> loanDetails = bankingSystemInstance.getLoanDetails("BANK_NAME", "BORROWER_NAME", 2);
        Assertions.assertTrue(loanDetails.isPresent());
        LoanDetails details = loanDetails.get();
        Assertions.assertEquals("BANK_NAME", details.getBankName());
        Assertions.assertEquals("BORROWER_NAME", details.getBorrowerName());
        Assertions.assertEquals(0, details.getPaidAmount());
        Assertions.assertEquals(60, details.getRemainingInstallmentCount());
    }

    @Test
    void testExecuteExistingLoanWithSameBank() {
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME BORROWER_NAME 10000 5 4"));
        Assertions.assertThrows(LoanAlreadyExistsException.class, () -> loanCommandExecutor.execute(new Command("LOAN BANK_NAME BORROWER_NAME 10000 5 4")));

    }

    @Test
    void testExecuteMultipleLoansSuccess() {
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME BORROWER_NAME 10000 5 4"));
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME1 BORROWER_NAME1 30000 2 3"));
        loanCommandExecutor.execute(new Command("LOAN BANK_NAME2 BORROWER_NAME2 40000 1 2"));

        Assertions.assertEquals(3, bankingSystemInstance.getBankLoans().size());
    }
}