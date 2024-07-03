package com.example.askme.api.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.askme.common.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public CompletableFuture<URL> upload(MultipartFile image) throws IOException {
        if (image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isBlank())
            throw new BusinessException(BAD_REQUEST_IMAGE);
        validateExtension(image.getOriginalFilename());
        return this.uploadImageToS3(image);
    }

    @Async
    public CompletableFuture<URL> uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream inputStream = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BusinessException(INVALID_AWS_CONNECTION);
        } finally {
            byteArrayInputStream.close();
            inputStream.close();
        }
        return CompletableFuture.completedFuture(amazonS3Client.getUrl(bucketName, s3FileName));
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
