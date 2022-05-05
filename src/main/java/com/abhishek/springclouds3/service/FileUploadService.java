package com.abhishek.springclouds3.service;

import com.abhishek.springclouds3.common.AWSS3FileManager;
import com.abhishek.springclouds3.dto.FileInfo;
import com.abhishek.springclouds3.entity.FileInfoEntity;
import com.abhishek.springclouds3.repository.FileInfoRepository;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Service
public class FileUploadService {

    private final FileInfoRepository fileInfoRepository;

    private final String bucketName;

    public FileUploadService(
            FileInfoRepository fileInfoRepository,
            @Value("${s3.bucket.name}") String bucketName) {
        this.fileInfoRepository = fileInfoRepository;
        this.bucketName = bucketName;
    }

    public FileInfo uploadFile(MultipartFile multipartFile) throws IOException {
        log.info("operation = uploadFile, result = IN_PROGRESS, fileName = {}", multipartFile.getOriginalFilename());

        String version = AWSS3FileManager.postS3Content(bucketName, multipartFile.getOriginalFilename(), multipartFile.getInputStream());

        FileInfoEntity fileInfoEntity = new FileInfoEntity();
        fileInfoEntity.setName(multipartFile.getOriginalFilename());
        fileInfoEntity.setFileVersion(Optional.ofNullable(version).orElse(""));

        fileInfoEntity = fileInfoRepository.save(fileInfoEntity);

        FileInfo fileInfo = new FileInfo();

        BeanUtils.copyProperties(fileInfoEntity, fileInfo);

        log.info("operation = uploadFile, result = SUCCESS, fileName = {}", multipartFile.getOriginalFilename());
        return fileInfo;
    }

    public void deleteFile(String fileName) {
        log.info("operation = deleteFile, result = IN_PROGRESS, fileName = {}", fileName);
        FileInfoEntity fileInfoEntity = fileInfoRepository.findByName(fileName);

        AWSS3FileManager.deleteS3FileContent(bucketName, fileName);

        fileInfoRepository.delete(fileInfoEntity);
        log.info("operation = deleteFile, result = SUCCESS, fileName = {}", fileName);
    }

    public String getPreSignedUrl(String fileName) {
        log.info("operation = getPreSignedUrl, result = IN_PROGRESS, fileName = {}", fileName);

        String url = AWSS3FileManager.getPreSignedUrl(bucketName, fileName);

        log.info("operation = getPreSignedUrl, result = SUCCESS, fileName = {}", fileName);
        return url;
    }

    public byte[] getStreamImage(String fileName) throws IOException {
        log.info("operation = getStreamImage, result = IN_PROGRESS, fileName = {}", fileName);

       InputStream inputStream =  AWSS3FileManager.getS3FileContentIS(bucketName, fileName);

        log.info("operation = getStreamImage, result = SUCCESS, fileName = {}", fileName);

        return IOUtils.toByteArray(inputStream);
    }
}
