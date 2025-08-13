package br.com.moreira.desafiopetize.exceptions;

public class IncorrectFileNameException extends RuntimeException {
    public IncorrectFileNameException(String message) {
        super(message);
    }
}
