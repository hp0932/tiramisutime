package com.custard.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.custard.entity.BoardEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
	
	private Long id;
	
	private String title;
	private String ownerId;
	private String ownerName;
	private String content;
	
	private Integer readCnt;
	
	public BoardEntity toEntity() {
		BoardEntity build = BoardEntity.builder()
				.id(id)
				.title(title)
				.ownerId(ownerId)
				.ownerName(ownerName)
				.content(content)
				.readCnt(readCnt)
				.build();
		return build;
	}

	@Builder
	public BoardDto(Long id, String title, String content, String ownerId, String ownerName, Integer readCnt) {
		this.id = id;
		this.title = title;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.content = content;
		this.readCnt = readCnt;
	}
}
