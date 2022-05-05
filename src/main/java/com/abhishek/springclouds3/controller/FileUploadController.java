package com.abhishek.springclouds3.controller;


import com.abhishek.springclouds3.dto.FileInfo;
import com.abhishek.springclouds3.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/s3")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileInfo> uploadFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        log.info("api = /s3, method = POST, result = IN_PROGRESS, fileName = {}", multipartFile.getOriginalFilename());

        FileInfo fileInfo = fileUploadService.uploadFile(multipartFile);

        log.info("api = /s3, method = POST, result = SUCCESS, fileName = {}", multipartFile.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.CREATED).body(fileInfo);
    }

    @DeleteMapping(value = "/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileName") String fileName) {
        log.info("api = /s3, method = DELETE, result = IN_PROGRESS, fileName = {}", fileName);

        fileUploadService.deleteFile(fileName);

        log.info("api = /s3, method = POST, result = SUCCESS, fileName = {}", fileName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/preSignedUrl")
    public ResponseEntity<String> getPreSignedUrl(@RequestParam("fileName") String fileName) {
        log.info("api = /s3/preSignedUrl, method = GET, result = IN_PROGRESS, fileName = {}", fileName);

        String url = fileUploadService.getPreSignedUrl(fileName);

        log.info("api = /s3/preSignedUrl, method = GET, result = SUCCESS, fileName = {}", fileName);
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }

    @GetMapping(value = "/streamImage",
            produces = {MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getStreamImage(@RequestParam("fileName") String fileName) throws IOException {
        log.info("api = /s3/streamImage, method = GET, result = IN_PROGRESS, fileName = {}", fileName);

        byte[] byteArray = fileUploadService.getStreamImage(fileName);

//        new FileInputStream(new File(getClass().getResource("/abc.png").toURI()));
//        InputStream in = getClass().getResourceAsStream("/abc.png");
//        return IOUtils.toByteArray(in);

        log.info("api = /s3/streamImage, method = GET, result = SUCCESS, fileName = {}", fileName);

        return byteArray;
    }
}
