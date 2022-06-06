package com.ledger.co.reader;

import com.ledger.co.model.BankingSystem;
import com.ledger.co.service.BankingOperationService;
import com.ledger.co.commands.CommandExecutorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FileInputReaderTest {

    private FileInputReader fileInputReader;
    private final CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(new BankingOperationService(BankingSystem.getBankingSystemInstance()));

    @Test
    void testProcessWithValidFileShouldReadSuccessfully() throws IOException {
        fileInputReader = new FileInputReader(commandExecutorFactory,"input.txt");
        fileInputReader.process();
    }

    @Test
    void testProcessWithUnAvailableFileShouldThrowException() throws IOException {
        fileInputReader = new FileInputReader(commandExecutorFactory,"unavailable.txt");
        Assertions.assertThrows(FileNotFoundException.class, () -> fileInputReader.process());
    }

}
