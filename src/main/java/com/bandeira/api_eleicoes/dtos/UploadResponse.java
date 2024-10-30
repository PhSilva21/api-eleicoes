package com.bandeira.api_eleicoes.dtos;

import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public record UploadResponse(

        PutObjectResponse response,

        String errorMessage,

        String location) {

}
