package com.api.fileloader.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "hash_sha_256")
    private String hashSHA256;

    @Column(name = "hash_sha_512")
    private String hashSHA512;

    @Column(name = "last_upload")
    private LocalDateTime lastUpload;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;

    public File(String fileName, String hashSHA256, String hashSHA512, LocalDateTime lastUpload, User user) {
        this.fileName = fileName;
        this.hashSHA256 = hashSHA256;
        this.hashSHA512 = hashSHA512;
        this.lastUpload = lastUpload;
        this.user = user;
    }
}

