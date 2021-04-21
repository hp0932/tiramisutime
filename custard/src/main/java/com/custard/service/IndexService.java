package com.custard.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.custard.dto.BoardDto;
import com.custard.dto.RedisIndexDto;
import com.custard.entity.RedisIndexEntity;
import com.custard.repository.RedisIndexRepository;

@Service
public class IndexService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

	private static final int PAGE_SIZE = 10;
	
	@Autowired
	RedisIndexRepository redisIndexRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * 현재 방문자수 +1 및 return
	 * @return int
	 */
	public int getIndexVisiter() {
		
		RedisIndexDto dto = new RedisIndexDto();

		//visiter값 없을경우 set 1
		try {
			redisIndexRepo.findByName("visiters");
		} catch (Exception e) {
			dto.setName("visiters");
			dto.setVisiters(1);
		}
		
		//visiters read
		RedisIndexEntity redisIndexEntity = redisIndexRepo.findByName("visiters");
		dto = modelMapper.map(redisIndexEntity, RedisIndexDto.class);
		
		String name = dto.getName();
		int visiters = dto.getVisiters();
		
		//visiters +1
		visiters = visiters + 1;
		
		dto.setName(name);
		dto.setVisiters(visiters);
		
		//visiters write
		redisIndexRepo.save(dto.toEntity());
		
		return visiters;
	}
	
}
