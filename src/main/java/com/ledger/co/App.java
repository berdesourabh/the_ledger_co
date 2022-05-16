package com.ledger.co;

import com.ledger.co.reader.FileInputReader;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        //final String fileName = args[0];
        new FileInputReader("input.txt").process();
    }
}
