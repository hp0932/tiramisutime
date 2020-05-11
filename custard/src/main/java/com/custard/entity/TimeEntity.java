package com.custard.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeEntity {
	
	@CreatedDate
	@Column(name = "create_date", updatable = false)
	private Timestamp createDate;
	
	@LastModifiedDate
	@Column(name = "update_date")
	private Timestamp updateDate;
}
