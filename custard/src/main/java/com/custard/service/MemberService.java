package com.custard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.custard.common.CryptoUtil;
import com.custard.dto.LevelDto;
import com.custard.dto.MemberDto;
import com.custard.entity.MemberEntity;
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
	 * @param memberDto
	 * @return Long id || -1: 아이디 중복, -2: 이름 중복, -3: 이메일 중복
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
	 * 비밀번호 확인
	 * @param String password
	 * @param session String userId
	 * @return int 1 || int 0: 패스워드 오류 
	 */
	public int getPassTest(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		String password = request.getParameter("password");
		MemberDto dto = modelMapper.map(memberRepo.findByUserId(session.getAttribute("userId").toString()), MemberDto.class);
		String getPass = dto.getPassword();
		
		//입력한 패스워드가 같을 경우 return 1 아닐 경우 return 0
		if(password.equals(getPass)) {
			return 1;
		}else {
			return 0;
		}
	}
	
	/**
	 * 회원정보 단건조회
	 * @param session String userId
	 * @return memberDto
	 */
	public MemberDto getOneUser(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		String userId = session.getAttribute("userId").toString();
		return modelMapper.map(memberRepo.findByUserId(userId), MemberDto.class);
	}
	
	/**
	 * 회원정보 수정
	 * @param String userId, String password, String email, int level
	 * @param session String userId
	 * @return Long id || -1: 사용자 아이디 오류, -2: 닉네임 중복, -3: 이메일 중복
	 */
	public Long setMemberUpdate(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		MemberDto dto = insertDto(request, params);
		MemberDto old = modelMapper.map(memberRepo.findByUserId(session.getAttribute("userId").toString()), MemberDto.class);
		
		//아이디값 다시 집어넣기(아이디는 변경되어서는 안됨)
		dto.setId(old.getId());
		
		//이름값이 다르다면
		if(!dto.getName().equals(old.getName())) {
			//이름 중복검사
			MemberDto nameTest = modelMapper.map(memberRepo.findByName(dto.getName()), MemberDto.class);
			logger.debug("name test >>> {}", nameTest);
			if(nameTest != null) {
				return (long) -2; 
			}else {
				logger.debug("name check >>> OK");
			}
		}
		//이메일값이 다르다면
		if(!dto.getEmail().equals(old.getEmail())) {
			//이메일 중복검사
			MemberDto emailTest = modelMapper.map(memberRepo.findByEmail(dto.getEmail()), MemberDto.class);
			logger.debug("email test >>> {}", emailTest);
			if(emailTest != null) {
				return (long) -3;
			}else {
				logger.debug("email check >>> OK");
			}
		}
		return memberRepo.save(dto.toEntity()).getId();
	}
	
	/**
	 * 로그인 구현
	 * @param String userId
	 * @param String password
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
	 * @return List(memberDto)
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
	 * @return List(level data)
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
	 * @param int level
	 * @param String levelName
	 */
	public void setLevel(int level, String levelName) {
		LevelDto dto = new LevelDto();
		dto.setLevel(level);
		dto.setLevelName(levelName);
		
		levelRepo.save(dto.toEntity());
	}
	/**
	 * 레벨 업데이트
	 * @param Long id
	 * @param int level
	 * @param String levelName
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
	 * @param String userId
	 * @param String password
	 * @param String email
	 * @param String name
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
