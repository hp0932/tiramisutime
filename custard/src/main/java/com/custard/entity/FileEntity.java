package com.custard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "file")
public class FileEntity extends TimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "file_thread", nullable = false)
	private Long fileThread;
	
	@Column(name = "file_name", nullable = false)
	private String fileName;
	
	@Column(name = "save_name", nullable = false)
	private String saveName;
	
	@Column(name = "file_size")
	private Long fileSize;
	
	@Column(name = "file_uploader", nullable = false)
	private String fileUploader;
	
	@Builder
	public FileEntity(Long id, Long fileThread, String fileName, String saveName, Long fileSize, String fileUploader) {
		this.id = id;
		this.fileThread = fileThread;
		this.fileName = fileName;
		this.saveName = saveName;
		this.fileSize = fileSize;
		this.fileUploader = fileUploader;
	}
}
