package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.model.Profile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Service
public class ImageService {
    private final String FOLDER = "profileImages/";

    @Autowired
    private AmazonS3 s3Client;

    @Value("${do.space.bucket}")
    private String doSpaceBucket;

    public Profile profileUpload(MultipartFile multipartFile, Profile profile) throws IOException {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String imgName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
        imgName = "profile" + "_" + System.currentTimeMillis();
        String key = FOLDER + imgName + "." + extension;
        saveImageToServer(multipartFile, key);
        profile.setProfilePic(key);
        return profile;
    }

    private void saveImageToServer(MultipartFile multipartFile, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getInputStream().available());
        if (multipartFile.getContentType() != null &&
                !"".equals(multipartFile.getContentType())) {
            metadata.setContentType(multipartFile.getContentType());
        }
        // Create a TransferManager
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        // Get the file's input stream
        InputStream inputStream = multipartFile.getInputStream();
        // Create a PutObjectRequest for the target bucket and key
        PutObjectRequest putObjectRequest = new PutObjectRequest(doSpaceBucket, key, inputStream, metadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        // Upload the file
        Transfer transfer = transferManager.upload(putObjectRequest);
        // Wait for the upload to complete
        try {
            transfer.waitForCompletion();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
