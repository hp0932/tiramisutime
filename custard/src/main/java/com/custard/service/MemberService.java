package com.custard.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.custard.common.CryptoUtil;
import com.custard.dto.BoardDto;
import com.custard.dto.LevelDto;
import com.custard.dto.MemberDto;
import com.custard.entity.BoardEntity;
import com.custard.entity.MemberEntity;
import com.custard.repository.BoardRepository;
import com.custard.repository.LevelRepository;
import com.custard.repository.MemberRepository;

@Service
public class MemberService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
	private static final int DEFAULT_LEVEL = 1;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LevelRepository levelRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * 회원가입 구현
	 * @param request
	 * @param params
	 * @return Long id
	 * @throws Exception
	 */
	@Transactional
	public Long setMemberJoin(HttpServletRequest request, @RequestParam Map params) throws Exception {
		HashMap result = new HashMap();
		
		MemberDto dto = insertDto(request, params);
		logger.debug("user join dto >>> {}", dto);
		
		//유효성검사
		try {
			//아이디 중복검사
			MemberDto idTest = modelMapper.map(memberRepo.findByUserId(dto.getUserId()), MemberDto.class);
			logger.debug("id test >>> {}", idTest);
			if(idTest != null) {
				return (long) -1;
			}else {
				logger.debug("user id check >> OK");
			}
		} catch (Exception e) {
			logger.debug("id test exception catch");
		}
		try {
			//이름 중복검사
			MemberDto nameTest = modelMapper.map(memberRepo.findByName(dto.getName()), MemberDto.class);
			logger.debug("name test >>> {}", nameTest);
			if(nameTest != null) {
				return (long) -2; 
			}else {
				logger.debug("name check >>> OK");
			}
		} catch (Exception e) {
			logger.debug("name test exception catch");
		}
		try {
			//이메일 중복검사
			MemberDto emailTest = modelMapper.map(memberRepo.findByEmail(dto.getEmail()), MemberDto.class);
			logger.debug("email test >>> {}", emailTest);
			if(emailTest != null) {
				return (long) -3;
			}else {
				logger.debug("email check >>> OK");
			}
		} catch (Exception e) {
			logger.debug("email test exception catch");
		}
		
		CryptoUtil crypto = new CryptoUtil();
		String cryptoPassword = crypto.AES_Encode(dto.getPassword());
		dto.setPassword(cryptoPassword);
		
		logger.debug("user join dto(crypt) >>> {}", dto);
		return memberRepo.save(dto.toEntity()).getId();
	}
	
	/**
	 * 로그인 구현
	 * @param request
	 * @param params
	 * @param session
	 * @return user level
	 * @throws Exception
	 */
	public int getLogin(HttpServletRequest request, @RequestParam Map params, HttpSession session) throws Exception {
		int level = -1;
		
		CryptoUtil crypto = new CryptoUtil();
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String cryptoPassword = crypto.AES_Encode(password);
		logger.debug("user login >>> {} / {}", userId, password);
		try {
			MemberDto dto = modelMapper.map(memberRepo.findByUserId(userId), MemberDto.class);
			logger.debug("now, login user dto data >>> {}", dto);
			logger.debug("password match test >>> {} / {}", dto.getPassword(), cryptoPassword);
			if(cryptoPassword.equals(dto.getPassword())) {
				session.setAttribute("userId", dto.getUserId());
				session.setAttribute("name", dto.getName());
				session.setAttribute("level", dto.getLevel());
				level = dto.getLevel();
			}
		} catch (Exception e) {
		}
		
		return level;
	}
	
	/**
	 * 멤버리스트 조회
	 * @param request
	 * @param params
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Map getMemberList(HttpServletRequest request, @RequestParam Map params, HttpSession session) throws Exception {
		Map result = new HashMap();
		
		List memberList = memberRepo.findAll();
		
		result.put("memberList", memberList);
		return result;
	}
	
	/**
	 * 유저 레벨 저장
	 * @param id
	 * @param level
	 */
	public void setUserLevel(Long id, int level) {
		MemberDto dto = new MemberDto();
		Optional<MemberEntity> user = memberRepo.findById(id);
		MemberEntity entity = user.get();
		
		dto.setId(id);
		dto.setUserId(entity.getUserId());
		dto.setPassword(entity.getPassword());
		dto.setEmail(entity.getEmail());
		dto.setName(entity.getName());
		dto.setLevel(level);
		
		
		memberRepo.save(dto.toEntity());
	}
	
	/**
	 * 레벨 리스트 조회
	 * @param request
	 * @param params
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Map getLevelList(HttpServletRequest request, @RequestParam Map params, HttpSession session) throws Exception {
		Map result = new HashMap();
		
		List levelList = levelRepo.findAll();
		
		result.put("levelList", levelList);
		return result;
	}
	
	/**
	 * 레벨 저장
	 * @param request
	 * @param params
	 */
	public void setLevel(int level, String levelName) {
		LevelDto dto = new LevelDto();
		dto.setLevel(level);
		dto.setLevelName(levelName);
		
		levelRepo.save(dto.toEntity());
	}
	/**
	 * 레벨 업데이트
	 * @param id
	 * @param level
	 * @param levelName
	 */
	public void setLevel(Long id, int level, String levelName) {
		LevelDto dto = new LevelDto();
		dto.setId(id);
		dto.setLevel(level);
		dto.setLevelName(levelName);
		
		levelRepo.save(dto.toEntity());
	}
	
	/**
	 * 회원가입을 위한 DTO처리
	 * @param request
	 * @param params
	 * @return DTO에 자료를 담아 return
	 */
	public MemberDto insertDto(HttpServletRequest request, @RequestParam Map params) {
		MemberDto dto = new MemberDto();
		
		dto.setUserId(request.getParameter("userId"));
		dto.setPassword(request.getParameter("password"));
		dto.setEmail(request.getParameter("email"));
		dto.setName(request.getParameter("name"));
		dto.setLevel(DEFAULT_LEVEL);
		
		return dto;
	}
}
