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
import java.util.concurrent.ExecutionException;

import static com.example.askme.common.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isBlank())
            throw new BusinessException(BAD_REQUEST_IMAGE);

        validateExtension(image.getOriginalFilename());

        try {
            URL uploadedUrl = uploadImageToS3(image).get();
            return uploadedUrl.toString();
        } catch (ExecutionException | InterruptedException | IOException e) {
            log.error("S3 업로드에 실패했습니다.", e);
            throw new BusinessException(UPLOAD_FAILED, e);
        }
    }

    @Async
    public CompletableFuture<URL> uploadImageToS3(MultipartFile image) throws IOException {

        log.info("이미지 업로드 시작");
        String originalFilename = image.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

        try (InputStream inputStream = image.getInputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(inputStream))) {

            byte[] bytes = IOUtils.toByteArray(inputStream);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(bytes.length);

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);

            return CompletableFuture.completedFuture(amazonS3Client.getUrl(bucketName, s3FileName));
        } catch (Exception e) {
            log.error("S3 업로드 중 예외 발생", e);
            throw new BusinessException(INVALID_AWS_CONNECTION, e);
        }
    }

    private void validateExtension(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "gif", "png");

        if (!allowedExtensions.contains(extension)) {
            throw new BusinessException(INVALID_FILE_EXTENSION);
        }
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf(".");

        if(index == -1 || index == filename.length() - 1) {
            throw new BusinessException(INVALID_FILE_EXTENSION);
        }

        return filename.substring(index + 1);
    }

}
