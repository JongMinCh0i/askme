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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.example.askme.common.error.ErrorCode.INVALID_AWS_CONNECTION;

@Slf4j
@Service
@Async
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public CompletableFuture<URL> uploadFile(MultipartFile image) {
        log.info("업로드 파일 이미지 이름 = {}", image.getOriginalFilename());
        return CompletableFuture.supplyAsync(() -> {
            log.info("이미지 업로드 시작, 이미지 이름 = {}", image.getOriginalFilename());
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

            InputStream inputStream = null;

            try {
                inputStream = image.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);

            byte[] bytes = null;

            try {
                bytes = IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(putObjectRequest);
            } catch (Exception e) {
                throw new BusinessException(INVALID_AWS_CONNECTION);
            } finally {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            URL url = amazonS3Client.getUrl(bucketName, fileName);
            log.info("이미지 업로드 완료, 이미지 URL = {}", url);
            return url;
        });
    }
}
