package com.api.fileloader.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UploadResponse {
    private String algorithm;
    private List<Document> documents;
}
