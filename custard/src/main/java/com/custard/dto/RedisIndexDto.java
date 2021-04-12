package com.custard.dto;

import com.custard.entity.RedisIndexEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RedisIndexDto {
	
	private Long id;
	
	private String name;
	
	private int visiters;
	
	public RedisIndexEntity toEntity() {
		RedisIndexEntity build = RedisIndexEntity.builder()
				.id(id)
				.name(name)
				.visiters(visiters)
				.build();
		return build;
	}
	
	@Builder
	public RedisIndexDto (Long id, String name, int visiters) {
		this.id = id;
		this.name = name;
		this.visiters = visiters;
	}

}
