package com.custard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.custard.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	MemberEntity findByUserId(@Param("userId") String userId);
	MemberEntity findByEmail(@Param("email") String email);
	MemberEntity findByName(@Param("name") String name);
}