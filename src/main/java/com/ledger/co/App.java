package com.ledger.co;

import com.ledger.co.commands.CommandExecutorFactory;
import com.ledger.co.model.BankingSystem;
import com.ledger.co.reader.FileInputReader;
import com.ledger.co.service.BankingOperationService;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        final String fileName = args[0];
        BankingSystem bankingSystem = BankingSystem.getBankingSystemInstance();
        BankingOperationService bankingOperationService = new BankingOperationService(bankingSystem);
        CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(bankingOperationService);
        new FileInputReader(commandExecutorFactory,fileName).process();
    }
}
