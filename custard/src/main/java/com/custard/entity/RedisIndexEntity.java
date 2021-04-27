package com.custard.entity;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("indexVisiters")
public class RedisIndexEntity {
	
	@Id
	@Indexed
	private String id;
	
	private String name;
	
	private int visiters;
	
	@Builder
	public RedisIndexEntity(String id, String name, int visiters) {
		this.id = id;
		this.name = name;
		this.visiters = visiters;
	}
	
}
