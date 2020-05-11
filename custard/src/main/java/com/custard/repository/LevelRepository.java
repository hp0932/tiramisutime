package com.custard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.custard.entity.LevelEntity;

public interface LevelRepository extends JpaRepository<LevelEntity, Long> {
}