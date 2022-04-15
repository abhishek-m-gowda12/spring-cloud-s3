package com.abhishek.springclouds3.service;

import com.abhishek.springclouds3.repository.StudentRepository;
import org.springframework.stereotype.Service;


@Service
public class FileUploadService {

    private final StudentRepository studentRepository;

    public FileUploadService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void uploadFile() {
    }

    public void deleteFile(String fileName) {
    }
}
