package com.custard.entity;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("redisIndex")
public class RedisIndexEntity {
	
	@Id
	private Long id;
	
	private String name;
	
	private int visiters;
	
	@Builder
	public RedisIndexEntity(Long id, String name, int visiters) {
		this.id = id;
		this.name = name;
		this.visiters = visiters;
	}
	
}
