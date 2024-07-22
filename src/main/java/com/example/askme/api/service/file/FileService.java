package com.example.askme.api.service.file;

import com.example.askme.common.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.example.askme.common.error.ErrorCode.BAD_REQUEST_IMAGE;
import static com.example.askme.common.error.ErrorCode.INVALID_FILE_EXTENSION;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorageService fileStorageService;

    public List<String> upload(List<MultipartFile> images) {

        List<String> uploadedUrls = new ArrayList<>();
        List<CompletableFuture<URL>> futures = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isBlank())
                throw new BusinessException(BAD_REQUEST_IMAGE);
            validateExtension(image.getOriginalFilename());

            try {
                log.info("futures.add= {}", image.getOriginalFilename());
                futures.add(fileStorageService.uploadFile(image));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (CompletableFuture<URL> future : futures) {
            String string = future.join().toString();
            log.info("uploadedUrls.add= {}", string);
            uploadedUrls.add(string);
        }

        return uploadedUrls;
    }


    private void validateExtension(String filename) {
        String[] parts = filename.toLowerCase().split("\\.");

        if (parts.length > 1) {
            String extension = parts[parts.length - 1];
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "gif", "png");

            if (!allowedExtensions.contains(extension)) {
                throw new BusinessException(INVALID_FILE_EXTENSION);
            }
        }
    }
}
