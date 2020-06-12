package com.custard.dto;

import com.custard.entity.FileEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileViewDto {
	
	private Long id;
	
	private String fileName;
	private String fileUploader;
	
	public FileEntity toEntity() {
		FileEntity build = FileEntity.builder()
				.id(id)
				.fileName(fileName)
				.fileUploader(fileUploader)
				.build();
		return build;
	}
	
	@Builder
	public FileViewDto(Long id, String fileName, String fileUploader) {
		this.id = id;
		this.fileName = fileName;
		this.fileUploader = fileUploader;
	}
}
