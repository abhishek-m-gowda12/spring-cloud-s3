package com.abhishek.springclouds3.repository;

import com.abhishek.springclouds3.entity.FileInfoEntity;
import org.springframework.data.repository.CrudRepository;

public interface FileInfoRepository extends CrudRepository<FileInfoEntity, Long> {
    FileInfoEntity findByName(String name);
}
