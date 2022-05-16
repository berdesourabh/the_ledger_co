package com.ledger.co.reader;

import com.ledger.co.model.Command;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class FileInputReader extends InputReader {
    private static Logger LOGGER = Logger.getLogger("FileInputReader");
    private final String fileName;

    public FileInputReader(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public void process() throws IOException {
        LOGGER.info("Reading input file: " + fileName);
        final File inputFile = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String inputLine = reader.readLine();
            while (inputLine != null) {
                final Command command = new Command(inputLine);
                LOGGER.info("Processing command: " + command.getCommandName());
                inputLine = reader.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileNotFoundException();
        }
    }
}
