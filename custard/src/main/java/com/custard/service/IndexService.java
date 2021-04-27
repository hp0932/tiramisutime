package com.custard.service;

import java.util.Optional;

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
		RedisIndexEntity redisIndexEntity;
		
		boolean indexFisrtTest = redisIndexRepo.existsById("visiters");
		
		logger.debug("index first load test >>> {}", indexFisrtTest);
		
		//visiter값 없을경우 set 1
		if(indexFisrtTest) {
			//visiters 값이 있을 경우 가져옴
			redisIndexEntity = redisIndexRepo.findById("visiters").get();
			dto = modelMapper.map(redisIndexEntity, RedisIndexDto.class);
			logger.debug("visiters load test >>> {} / {}", dto.getId(), dto.getVisiters());
		}else {
			//visiters 값이 없을 경우 visiters에 1을 투입
			logger.debug("[CATCH]index visiter first load");
			dto.setId("visiters");
			dto.setVisiters(1);
			redisIndexRepo.save(dto.toEntity());
			logger.debug("[CATCH]index visiter first save");
		}

		
		//visiters read
		redisIndexEntity = redisIndexRepo.findById("visiters").get();
		//dto = modelMapper.map(redisIndexEntity, RedisIndexDto.class);
		
		String id = dto.getId();
		int visiters = dto.getVisiters();
		
		logger.debug("redis id >>> {} / redis visiters >>> {} ", id, visiters);
		
		//visiters +1
		visiters = visiters + 1;
		
		dto.setId(id);
		dto.setVisiters(visiters);
		
		logger.debug("redis id >>> {} / redis visiters >>> {} ", id, visiters);
		
		//visiters write
		redisIndexRepo.save(dto.toEntity());
		
		return visiters;
	}
	
}
