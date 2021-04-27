package com.custard.repository;

import org.springframework.data.repository.CrudRepository;

import com.custard.entity.RedisIndexEntity;

public interface RedisIndexRepository extends CrudRepository<RedisIndexEntity, String> {
	
}
