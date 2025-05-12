package com.example.solidanalyzer.repository;

import com.example.solidanalyzer.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
