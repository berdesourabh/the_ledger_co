package com.ledger.co.commands;

import com.ledger.co.service.BankingOperationService;
import com.ledger.co.model.Command;

public abstract class CommandExecutor {
    public abstract boolean validate(Command command);

    public abstract void execute(Command command);
}
