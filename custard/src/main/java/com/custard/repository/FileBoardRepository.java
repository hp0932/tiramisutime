package com.custard.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.custard.entity.BoardEntity;
import com.custard.entity.FileBoardEntity;

public interface FileBoardRepository extends JpaRepository<FileBoardEntity, Long> {
	public List<FileBoardEntity> findByTitleLikeOrderByIdDesc(String title);
}