package com.ledger.co.reader;

import com.ledger.co.commands.CommandExecutorFactory;
import com.ledger.co.model.Command;

import java.io.*;
import java.util.logging.Logger;

public class FileInputReader extends InputReader {
    private static Logger LOGGER = Logger.getLogger("FileInputReader");
    private final String fileName;

    public FileInputReader(CommandExecutorFactory commandExecutorFactory, String fileName) {
        super(commandExecutorFactory);
        this.fileName = fileName;
    }


    @Override
    public void process() throws IOException {
        //LOGGER.info("Reading input file: " + fileName);
        final File inputFile = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String inputLine = reader.readLine();
            while (inputLine != null) {
                final Command command = new Command(inputLine);
                processCommand(command);
                inputLine = reader.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileNotFoundException();
        }
    }
}
