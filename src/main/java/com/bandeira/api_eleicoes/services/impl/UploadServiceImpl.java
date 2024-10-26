package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.services.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UploadServiceImpl implements UploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public UploadServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public UploadResponse uploadFile(MultipartFile file) {
        String key = file.getOriginalFilename();
        Path tempFile = null;

        try {
            tempFile = Files.createTempFile("upload-", key);
            file.transferTo(tempFile.toFile());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, tempFile);

            String location = String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

            return new UploadResponse(response, null, location);

        } catch (IOException e) {
            return new UploadResponse(null, "Erro ao fazer upload do arquivo: " + e.getMessage(), null);
        }
        catch (Exception e) {
            return new UploadResponse(null, "Erro inesperado: " + e.getMessage(), null);
        }
        finally {
            if (tempFile != null) {
                try {
                    Files.delete(tempFile);
                }
                catch (IOException e) {
                    System.err.println("Erro ao deletar arquivo temporário: " + e.getMessage());
                }
            }
        }
    }

}
