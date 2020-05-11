package com.custard.dto;

import com.custard.entity.LevelEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LevelDto {
	
	private Long id;
	
	private int level;
	private String levelName;
	
	public LevelEntity toEntity() {
		LevelEntity build = LevelEntity.builder()
				.id(id)
				.level(level)
				.levelName(levelName)
				.build();
		return build;
	}

	@Builder
	public LevelDto(Long id, int level, String levelName) {
		this.id = id;
		this.level = level;
		this.levelName = levelName;
	}
}
