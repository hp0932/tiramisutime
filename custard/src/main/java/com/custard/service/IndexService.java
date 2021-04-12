package com.custard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.custard.entity.RedisIndexEntity;
import com.custard.repository.RedisIndexRepository;

@Service
public class IndexService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

	private static final int PAGE_SIZE = 10;
	
	@Autowired
	RedisIndexRepository redisIndexRepo;
	
	public void GetIndexVisiter() {
		
		
		//redisIndexRepo.save();
	}
	
}
