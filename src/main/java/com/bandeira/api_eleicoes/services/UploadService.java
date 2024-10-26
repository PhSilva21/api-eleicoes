package com.bandeira.api_eleicoes.services;

import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.model.Candidate;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    UploadResponse uploadFile(MultipartFile file);
}
