package com.clip.assessment.exception;

public class ProcessException extends Exception {

    public ProcessException(Exception e) {
        super(e);
    }

    public ProcessException(String message) {
        super(message);
    }
}
