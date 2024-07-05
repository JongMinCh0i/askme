package com.example.askme.api.service.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.askme.common.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

        for (MultipartFile image : images) {
            if (image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isBlank())
                throw new BusinessException(BAD_REQUEST_IMAGE);
            validateExtension(image.getOriginalFilename());

            try {
                uploadedUrls.add(this.uploadImageToS3(image).join().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return uploadedUrls;
    }

    @Async
    public CompletableFuture<URL> uploadImageToS3(MultipartFile image) throws IOException {
        log.info("이미지 업로드 시작");
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream inputStream = image.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);

        return CompletableFuture.completedFuture(fileStorageService.uploadFile(s3FileName, inputStream, metadata));
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
