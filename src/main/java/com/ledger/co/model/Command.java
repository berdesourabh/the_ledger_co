package com.ledger.co.model;

import com.ledger.co.exception.InvalidCommandException;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Command {

    private String commandName;

    private List<String> params;


    public Command(final String inputLine) {
        List<String> paramList = Arrays.stream(inputLine.trim().split(" "))
                .map(String::trim)
                .filter(param -> (param.length() > 0)).collect(Collectors.toList());

        if(paramList.size()==0) {
            throw new InvalidCommandException();
        }

        commandName = paramList.get(0);
        paramList.remove(0);
        params = paramList;
    }
}
