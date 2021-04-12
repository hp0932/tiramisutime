package com.custard.dto;

import com.custard.entity.FileBoardEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileBoardDto {
	
	private Long id;
	
	private String title;
	private String ownerId;
	private String ownerName;
	private String content;
	
	private Integer readCnt;
	
	public FileBoardEntity toEntity() {
		FileBoardEntity build = FileBoardEntity.builder()
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
	public FileBoardDto(Long id, String title, String content, String ownerId, String ownerName, Integer readCnt) {
		this.id = id;
		this.title = title;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.content = content;
		this.readCnt = readCnt;
	}
}
