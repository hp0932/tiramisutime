package com.custard.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "email"})})
public class MemberEntity extends TimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "level", nullable = false)
	private int level;
	
	@Builder
	public MemberEntity(Long id, String userId, String password, String email, String name, int level) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.level = level;
	}
}
