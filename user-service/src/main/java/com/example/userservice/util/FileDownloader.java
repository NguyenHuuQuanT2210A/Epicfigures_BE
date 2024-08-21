package com.example.userservice.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloader {
    private static Path foundFile;

    public static Resource downloadFile(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("uploads");

        foundFile = Files.list(uploadDirectory)
                .filter(path -> path.getFileName().toString().startsWith(fileCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}
