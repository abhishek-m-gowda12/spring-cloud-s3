package com.abhishek.springclouds3.common;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AWSS3FileManager {

	public static String getS3FileContent(String bucketName, String fileName) throws IOException {

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();

		GetObjectRequest objectRequest = new GetObjectRequest(
				bucketName, fileName);

		S3Object fileObject = s3Client.getObject(objectRequest);

		InputStream objectData = fileObject.getObjectContent();

		return IOUtils.toString(objectData);
	}

	public static String getS3FileContentAsyncCall(String bucketName, String fileName) throws IOException {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
		GetObjectRequest objectRequest = new GetObjectRequest(
				bucketName, fileName);

		S3Object fileObject = s3Client.getObject(objectRequest);

		InputStream objectData = fileObject.getObjectContent();

		return IOUtils.toString(objectData);
	}
	public static String getS3FileContent(String bucketName, String fileName,String version) throws IOException {

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();

		GetObjectRequest objectRequest = new GetObjectRequest(
				bucketName, fileName);
		if(!StringUtils.isEmpty(version)) {
			objectRequest.setVersionId(version);
		}

		S3Object fileObject = s3Client.getObject(objectRequest);

		InputStream objectData = fileObject.getObjectContent();

		return IOUtils.toString(objectData);
	}

	public static InputStream getS3FileContentIS(String bucketName, String fileName) throws IOException {

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();

		GetObjectRequest objectRequest = new GetObjectRequest(
				bucketName, fileName);

		S3Object fileObject = s3Client.getObject(objectRequest);

		InputStream objectData = fileObject.getObjectContent();

		return objectData;
	}

	public static String postS3Content(String bucketName, String fileName, final File file) {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
        
		final PutObjectResult putObjectResult = s3Client.putObject( new PutObjectRequest(bucketName, fileName, file));		
		return putObjectResult.getVersionId();
	}

	public static String postS3FileContent(String bucketName, String fileName,String fileContent) {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();

		PutObjectResult putObjectResult = s3Client.putObject(bucketName, fileName, fileContent);
		return putObjectResult.getVersionId();
	}

	public static void deleteS3FileContent(String bucketName, String fileName) {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
		s3Client.deleteObject(bucketName, fileName);
	}
	
	public static void deleteS3FileBasedOnVersion(String bucketName, String fileName,String versionId) {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
		s3Client.deleteVersion(bucketName, fileName, versionId);
	}

	public static void getS3FolderContent(String bucketName, String folderName, File localDir) throws AmazonServiceException, AmazonClientException, InterruptedException {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
		
		TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();

		MultipleFileDownload download = transferManager.downloadDirectory(bucketName, folderName, localDir);
		download.waitForCompletion();
		
		transferManager.shutdownNow();
	}
}
