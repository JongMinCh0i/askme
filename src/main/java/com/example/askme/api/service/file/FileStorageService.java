package com.example.askme.api.service.file;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface FileStorageService {
    URL uploadFile(String fileName, InputStream inputStream, ObjectMetadata metadata) throws IOException;
}
