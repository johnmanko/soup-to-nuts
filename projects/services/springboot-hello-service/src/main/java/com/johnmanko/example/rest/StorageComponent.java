package com.johnmanko.example.rest;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class StorageComponent {

    //@Value("${app.storage.bucket-name}")
    //private String bucketName;

    //private S3Client s3Client;

    //@AutoWired
    //public StorageComponent(S3Client client) {
    //  s3Client = client;
    //}

    //public void addDocument(Document doc) {
    //      PutObjectRequest request = PutObjectRequest.builder()
    //                              .bucket(bucketName)
    //                              .key(pathAndFileNameKey)
    //                              .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
    //                              .build();
    //      s3Client.putObject(request), RequestBody.fromInputStream(fileInputStream, contentLength));.
    // }

}
