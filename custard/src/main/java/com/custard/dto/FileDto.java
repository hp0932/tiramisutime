package com.custard.dto;

import com.custard.entity.FileEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDto {
	
	private Long id;
	
	private Long fileThread;
	
	private String fileName;
	private String saveName;
	private Long fileSize;
	private String fileUploader;
	
	public FileEntity toEntity() {
		FileEntity build = FileEntity.builder()
				.id(id)
				.fileThread(fileThread)
				.fileName(fileName)
				.saveName(saveName)
				.fileSize(fileSize)
				.fileUploader(fileUploader)
				.build();
		return build;
	}
	
	@Builder
	public FileDto(Long id, Long fileThread, String fileName, String saveName, Long fileSize, String fileUploader) {
		this.id = id;
		this.fileThread = fileThread;
		this.fileName = fileName;
		this.saveName = saveName;
		this.fileSize = fileSize;
		this.fileUploader = fileUploader;
	}
}
