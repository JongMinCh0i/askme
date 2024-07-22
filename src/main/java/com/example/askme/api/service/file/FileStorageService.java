package com.example.askme.api.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface FileStorageService {
    CompletableFuture<URL> uploadFile(MultipartFile image) throws IOException;
}
