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
@Table(name = "fileshare")
public class FileViewEntity extends TimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "file_name", nullable = false)
	private String fileName;
	
	@Column(name = "file_uploader", nullable = false)
	private String fileUploader;
	
	@Builder
	public FileViewEntity(Long id, String fileName, String fileUploader) {
		this.id = id;
		this.fileName = fileName;
		this.fileUploader = fileUploader;
	}
}
