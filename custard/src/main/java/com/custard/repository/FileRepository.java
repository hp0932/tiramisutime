package com.custard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.custard.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
	List<FileEntity> findByFileThread(@Param("fileThread") Long fileTread);
}
