package com.api.fileloader.controller;

import com.api.fileloader.dto.response.Document;
import com.api.fileloader.dto.response.ErrorResponse;
import com.api.fileloader.dto.response.ListDocumentsResponse;
import com.api.fileloader.dto.response.UploadResponse;

import com.api.fileloader.exceptions.DocumentException;
import com.api.fileloader.service.DocumentService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/hash")
    public ResponseEntity<UploadResponse> uploadDocuments(@RequestParam List<MultipartFile> documents,
                                                          @RequestParam("hashType") String hashType,
                                                          @RequestHeader("Authorization") String token) throws IOException {
        UploadResponse response = documentService.uploadDocument(documents, hashType, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listDocuments(@RequestHeader("Authorization") String token,
                                                  @RequestParam(value = "hashType", required = false) String hashType,
                                                  @RequestParam(value = "hash", required = false) String hash) {
        if (hashType == null && hash == null) {
            List<ListDocumentsResponse> documentsResponses = documentService.listDocuments(token);
            return ResponseEntity.ok(documentsResponses);
        } else {
            Document document = documentService.fetchDocument(token, hashType, hash);
            return ResponseEntity.ok(document);
        }
    }

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<ErrorResponse> handleException(
            DocumentException e,
            HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(Instant.ofEpochSecond(System.currentTimeMillis()));
        errorResponse.setHttpStatus(e.getHttpStatus());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
}
