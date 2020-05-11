package com.custard.dto;

import java.sql.Timestamp;

import com.custard.entity.MemberEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
	
	private Long id;
	
	private String userId;
	private String password;
	private String email;
	private String name;
	private int level;
	
	public MemberEntity toEntity() {
		MemberEntity build = MemberEntity.builder()
				.id(id)
				.userId(userId)
				.password(password)
				.email(email)
				.name(name)
				.level(level)
				.build();
		return build;
	}

	@Builder
	public MemberDto(Long id, String userId, String password, String email, String name, int level) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.level = level;
	}
}
