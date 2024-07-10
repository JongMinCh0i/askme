package com.example.askme.api.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.askme.common.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.example.askme.common.error.ErrorCode.INVALID_AWS_CONNECTION;

@Service
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public URL uploadFile(String fileName, InputStream inputStream, ObjectMetadata metadata) throws IOException {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BusinessException(INVALID_AWS_CONNECTION);
        } finally {
            byteArrayInputStream.close();
        }

        return amazonS3Client.getUrl(bucketName, fileName);
    }
}
