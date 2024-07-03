package com.example.askme.api.controller.file;

import com.example.askme.api.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public CompletableFuture<URL> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("file: {}", file.getOriginalFilename());
        return fileService.uploadImageToS3(file);
    }
}
