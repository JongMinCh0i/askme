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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.example.askme.common.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorageService fileStorageService;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isBlank())
            throw new BusinessException(BAD_REQUEST_IMAGE);
        validateExtension(image.getOriginalFilename());

        try {
            return String.valueOf(this.uploadImageToS3(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public CompletableFuture<URL> uploadImageToS3(MultipartFile image) throws IOException {
        log.info("이미지 업로드 시작");
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

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
