package com.ledger.co.reader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FileInputReaderTest {

    private FileInputReader fileInputReader;

    @Test
    void testProcessWithValidFileShouldReadSuccessfully() throws IOException {
        fileInputReader = new FileInputReader("input.txt");
        fileInputReader.process();
    }

    @Test
    void testProcessWithUnAvailableFileShouldThrowException() throws IOException {
        fileInputReader = new FileInputReader("unavailable.txt");
        Assertions.assertThrows(FileNotFoundException.class, () -> fileInputReader.process());
    }

}
