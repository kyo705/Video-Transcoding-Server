package com.ktube.transcoding.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Repository
public class FileRepository {


    public boolean isExistingFile(String inputFilePath) {

        return Files.exists(Path.of(inputFilePath));
    }

    public boolean isExistingDirectory(String outputDirectoryPath) {

        return Files.isDirectory(Path.of(outputDirectoryPath));
    }

    public void createDirectory(String outputDirectoryPath) {

        try {
            Files.createDirectories(Path.of(outputDirectoryPath));
            log.info("Directory : {} is created now", outputDirectoryPath);
        } catch (IOException e) {
            log.error("Creating Directory : {} failed... cause by {}", outputDirectoryPath, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
