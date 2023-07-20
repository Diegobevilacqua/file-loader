package com.api.fileloader.mapper;

import com.api.fileloader.dto.response.Document;
import com.api.fileloader.dto.response.ListDocumentsResponse;
import com.api.fileloader.entity.File;

public class FileMapper {
    public static ListDocumentsResponse fileToListDocumentsResponse(File file) {
        if ( file == null ) {
            return null;
        }

        ListDocumentsResponse listDocumentsResponse = new ListDocumentsResponse();

        listDocumentsResponse.setFileName( file.getFileName() );
        listDocumentsResponse.setHashSha256( file.getHashSHA256() );
        listDocumentsResponse.setHashSha512( file.getHashSHA512() );
        listDocumentsResponse.setLastUpload( file.getLastUpload() );

        return listDocumentsResponse;
    }

    public static Document fileToDocument(File file) {
        if ( file == null ) {
            return null;
        }

        Document document = new Document();

        document.setFileName( file.getFileName() );
        document.setLastUpload( file.getLastUpload() );

        document.setHash( getHash(file.getHashSHA256(), file.getHashSHA512()) );

        return document;
    }

    private static String getHash(String hashSHA256, String hashSHA512) {
        return hashSHA256.isEmpty() ? hashSHA512 : hashSHA256;
    }
}
