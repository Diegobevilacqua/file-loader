package com.api.fileloader.service;

import com.api.fileloader.dto.response.Document;
import com.api.fileloader.dto.response.ListDocumentsResponse;
import com.api.fileloader.dto.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    UploadResponse uploadDocument(List<MultipartFile> documents, String hashType, String token) throws IOException;

    List<ListDocumentsResponse> listDocuments(String token);

    Document fetchDocument(String token, String hashType, String hash);
}
