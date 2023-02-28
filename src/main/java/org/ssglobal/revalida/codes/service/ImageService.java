package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.model.Profile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Service
public class ImageService {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${do.space.bucket}")
	private String doSpaceBucket;

	public Profile profileUpload(String directory2Upload, MultipartFile multipartFile, Profile profile)
			throws IOException {
		if (multipartFile.isEmpty()) {
			profile.setProfilePic("");
			return profile;
		}
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String imgName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
		imgName = "profile" + "_" + System.currentTimeMillis();
		String key = directory2Upload + imgName + "." + extension;
		saveImageToServer(multipartFile, key);
		profile.setProfilePic(key);
		return profile;
	}

	public Profile profileUpdate(String directory2Upload, MultipartFile multipartFile, Profile profile)
			throws IOException {
		if (multipartFile.isEmpty()) {
			return profile;
		}
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String imgName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
		imgName = "profile" + "_" + System.currentTimeMillis();
		String key = directory2Upload + imgName + "." + extension;
		updateImageToServer(multipartFile, key, profile.getProfilePic());
		profile.setProfilePic(key);
		return profile;
	}

	public Posts postUpload(String directory2Upload, MultipartFile multipartFile, Posts post) throws IOException {
		if (multipartFile.isEmpty()) {
			post.setImageUrl("");
			return post;
		}

		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String imgName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
		imgName = "post" + "_" + System.currentTimeMillis();
		String key = directory2Upload + imgName + "." + extension;
		saveImageToServer(multipartFile, key);
		post.setImageUrl(key);
		return post;
	}

	public Posts postUpdate(String directory2Upload, MultipartFile multipartFile, Posts post) throws IOException {

		if (multipartFile.isEmpty()) {
			return post;
		}

		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String imgName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
		imgName = "profile" + "_" + System.currentTimeMillis();
		;
		String key = directory2Upload + imgName + "." + extension;
		updateImageToServer(multipartFile, key, post.getImageUrl());
		post.setImageUrl(key);
		return post;
	}

	private void updateImageToServer(MultipartFile multipartFile, String key, String existingPic) throws IOException {
		if (existingPic != null && !existingPic.isEmpty()) {
			deleteImageFromServer(existingPic);
		}
		saveImageToServer(multipartFile, key);
	}

	private void deleteImageFromServer(String existingPic) {
		s3Client.deleteObject(doSpaceBucket, existingPic);
	}

	private void saveImageToServer(MultipartFile multipartFile, String key) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getInputStream().available());
		if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
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

	public String getImageUrl(String imageKey) {
		if (imageKey == null || imageKey.trim().isEmpty()) {
			return null;
		}
		boolean imageExists = s3Client.doesObjectExist(doSpaceBucket, imageKey);
		if (!imageExists) {
			return imageKey;
		}
		// Set the expiration time of the URL to one hour from now
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);
		// Generate the presigned URL
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(doSpaceBucket,
				imageKey).withMethod(HttpMethod.GET).withExpiration(expiration);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

}
