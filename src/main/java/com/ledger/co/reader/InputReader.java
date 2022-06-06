package com.ledger.co.reader;

import com.ledger.co.commands.CommandExecutor;
import com.ledger.co.commands.CommandExecutorFactory;
import com.ledger.co.exception.InvalidCommandException;
import com.ledger.co.model.Command;

import java.io.IOException;

public abstract class InputReader {

    private final CommandExecutorFactory commandExecutorFactory;

    protected InputReader(CommandExecutorFactory commandExecutorFactory) {
        this.commandExecutorFactory = commandExecutorFactory;
    }

    public abstract void process() throws IOException;

    protected void processCommand(Command command) {
        final CommandExecutor commandExecutor = commandExecutorFactory.getCommandExecutor(command);
        if(commandExecutor.validate(command)) {
            commandExecutor.execute(command);
        } else {
            throw new InvalidCommandException();
        }
    }
}
