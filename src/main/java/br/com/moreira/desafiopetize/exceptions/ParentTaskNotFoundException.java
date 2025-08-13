package br.com.moreira.desafiopetize.exceptions;

public class ParentTaskNotFoundException extends RuntimeException {
    public ParentTaskNotFoundException(String message) {
        super(message);
    }
}
