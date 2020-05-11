package com.custard.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.custard.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	public List<BoardEntity> findByTitleLikeOrderByIdDesc(String title);
}