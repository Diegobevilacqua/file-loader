package com.api.fileloader.service.impl;

import com.api.fileloader.dto.response.Document;
import com.api.fileloader.dto.response.ListDocumentsResponse;
import com.api.fileloader.dto.response.UploadResponse;
import com.api.fileloader.entity.File;
import com.api.fileloader.entity.User;
import com.api.fileloader.exceptions.DocumentNotFoundException;
import com.api.fileloader.exceptions.InvalidEncryptionAlgorithmException;
import com.api.fileloader.exceptions.UnsuccessfulUploadException;
import com.api.fileloader.mapper.FileMapper;
import com.api.fileloader.repository.FileRepository;
import com.api.fileloader.repository.UserRepository;
import com.api.fileloader.security.JWTUtils;
import com.api.fileloader.service.DocumentService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public UploadResponse uploadDocument(List<MultipartFile> documents, String hashType, String token) throws IOException {
        UploadResponse uploadResponse = new UploadResponse();
        List<Document> documentsList = new ArrayList<>();
        uploadResponse.setAlgorithm(hashType);

        try {
            if (!hashType.equals("SHA-256") && !hashType.equals("SHA-512")) {
                throw new InvalidEncryptionAlgorithmException();
            }

            String username = jwtUtils.extractUsername(token.substring(7));
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            log.info(String.format("User %s retrieved", user.getUsername()));

            for (MultipartFile document : documents) {
                String fileName = document.getOriginalFilename();
                String hash256 = DigestUtils.sha256Hex(document.getBytes());
                String hash512 = DigestUtils.sha512Hex(document.getBytes());
                Document doc = new Document();

                if (alreadyUploaded(username, hash256)) {
                    log.info(String.format("Updating last upload time of file %s for user %s", fileName, username));
                    fileRepository.updateLastUploadByHashAndUsername(hash256, username);
                    doc.setLastUpload(LocalDateTime.now());
                } else {
                    File file = new File();
                    file.setFileName(fileName);
                    file.setHashSHA256(hash256);
                    file.setHashSHA512(hash512);
                    file.setUser(user);
                    fileRepository.save(file);
                    log.info(String.format("File %s successfully uploaded.", file.getFileName()));
                }

                doc.setFileName(fileName);
                doc.setHash(hashType.equals("SHA-256") ? hash256 : hash512);
                documentsList.add(doc);
            }

            uploadResponse.setDocuments(documentsList);
        } catch (InvalidEncryptionAlgorithmException e) {
            throw new InvalidEncryptionAlgorithmException();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new UnsuccessfulUploadException();
        }

        return uploadResponse;
    }

    @Override
    public List<ListDocumentsResponse> listDocuments(String token) {
        String username = jwtUtils.extractUsername(token.substring(7));
        log.info(String.format("Retrieving documents of user %s", username));
        List<File> fileList = fileRepository.findByUserUsername(username);
        log.info(String.format("Documents of user %s successfully returned", username));

        return fileList
                .stream()
                .map(FileMapper::fileToListDocumentsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Document fetchDocument(String token, String hashType, String hash) {
        String username = jwtUtils.extractUsername(token.substring(7));
        List<File> fileList;

        try {
            if (hashType.equals("SHA-256")) {
                fileList = fileRepository
                        .findByHashSHA256AndUsername(hash, username)
                        .orElseThrow(DocumentNotFoundException::new);
            } else if (hashType.equals("SHA-512")){
                fileList = fileRepository
                        .findByHashSHA512AndUsername(hash, username)
                        .orElseThrow(DocumentNotFoundException::new);
            } else {
                throw new InvalidEncryptionAlgorithmException();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DocumentNotFoundException();
        }

        return FileMapper.fileToDocument(fileList.get(0));
    }

    private boolean alreadyUploaded(String username, String hashSHA256) {
        return !fileRepository.findByHashSHA256AndUsername(hashSHA256, username).get().isEmpty();
    }
}
