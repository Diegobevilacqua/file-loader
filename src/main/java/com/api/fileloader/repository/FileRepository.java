package com.api.fileloader.repository;

import com.api.fileloader.entity.File;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT f FROM File f JOIN f.user u WHERE u.username = :username")
    List<File> findByUserUsername(String username);

    @Query("SELECT f FROM File f JOIN f.user u WHERE u.username = :username AND f.hashSHA256 = :hashSHA256")
    Optional<List<File>> findByHashSHA256AndUsername(String hashSHA256, String username);

    @Query("SELECT f FROM File f JOIN f.user u WHERE u.username = :username AND f.hashSHA512 = :hashSHA512")
    Optional<List<File>> findByHashSHA512AndUsername(String hashSHA512, String username);

    @Transactional
    @Modifying
    @Query("UPDATE File f SET f.lastUpload = CURRENT_TIMESTAMP " +
            "WHERE f.hashSHA256 = :hashSHA256 " +
            "AND f.user IN (SELECT u.id FROM User u WHERE u.username = :username)")
    void updateLastUploadByHashAndUsername(String hashSHA256, String username);
}
