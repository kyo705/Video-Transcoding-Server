package com.ktube.transcoding.error;

public class NotExistingOriginalFileException extends RuntimeException {

    private final String notExistingOriginalFilePath;

    public NotExistingOriginalFileException(String notExistingOriginalFilePath) {

        this.notExistingOriginalFilePath = notExistingOriginalFilePath;
    }

    @Override
    public String toString() {
        return String.format("File Path : %s is not exist", notExistingOriginalFilePath);
    }

    public String getNotExistingOriginalFilePath() {

        return this.notExistingOriginalFilePath;
    }
}
