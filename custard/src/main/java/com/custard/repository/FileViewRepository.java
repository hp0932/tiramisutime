package com.custard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.custard.entity.FileViewEntity;

public interface FileViewRepository extends JpaRepository<FileViewEntity, Long> {
}
