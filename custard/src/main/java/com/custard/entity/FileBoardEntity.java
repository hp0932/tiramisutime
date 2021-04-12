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
@Table(name = "file_board")
public class FileBoardEntity extends TimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "owner_id", nullable = false)
	private String ownerId;
	
	@Column(name = "owner_name", nullable = false)
	private String ownerName;

	@Column(name = "content", columnDefinition = "TEXT" , nullable = false)
	private String content;
	
	@Column(name = "read_cnt", nullable = false)
	private Integer readCnt;
	
	@Builder
	public FileBoardEntity(Long id, String title, String ownerId, String ownerName, String content, Integer readCnt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.readCnt = readCnt;
	}
}
