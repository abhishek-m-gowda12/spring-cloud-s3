package com.abhishek.springclouds3.repository;

import com.abhishek.springclouds3.entity.StudentEntity;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<StudentEntity, Long> {
}
