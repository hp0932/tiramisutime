package com.custard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private static final CryptoUtil crypto = new CryptoUtil();
	private static final int DEFAULT_LEVEL = 1;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LevelRepository levelRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Value("${adminmail}")
	private String adminmail;
	@Value("${adminpass}")
	private String adminpass;
	
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
		String password = "";
		try {
			password = crypto.AES_Encode(request.getParameter("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		MemberDto dto = modelMapper.map(memberRepo.findByUserId(session.getAttribute("userId").toString()), MemberDto.class);
		String getPass = dto.getPassword();
		
		//패스워드를 입력하지 않았을 경우
		if(password == null) {
			return 0;
		}
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
	public Long setMemberUpdate(HttpServletRequest request, @RequestParam Map params, HttpSession session) throws Exception {
		MemberDto old = modelMapper.map(memberRepo.findByUserId(session.getAttribute("userId").toString()), MemberDto.class);
		MemberDto dto = updateDto(request, params, old);

		//아이디값 다시 집어넣기(아이디는 변경되어서는 안됨)
		dto.setId(old.getId());
		
		
		try {
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
		} catch (Exception e) {
			logger.debug("name test exception catch");
		}
		try {
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
		} catch (Exception e) {
			logger.debug("email test exception catch");
		}
		
		String cryptoPassword = crypto.AES_Encode(dto.getPassword());
		dto.setPassword(cryptoPassword);
		
		//세션 삭제
		session.invalidate();
		
		return memberRepo.save(dto.toEntity()).getId();
	}
	
	/**
	 * 이메일 입력
	 * @param String email
	 * @return 0: 정상처리 || -1: 없는 이메일
	 */
	public int setEmailCode(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		String email = request.getParameter("email");
		
		try {
			//이메일 중복검사
			MemberDto emailTest = modelMapper.map(memberRepo.findByEmail(email), MemberDto.class);
			logger.debug("email test >>> {}", emailTest);
			if(email == null) {
				return -1;
			}else {
				logger.debug("email check >>> OK");
				
			}
		} catch (Exception e) {
			logger.debug("email test exception catch");
		}
		
		return -1;
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
	
	/**
	 * 회원정보수정을 위한 DTO처리
	 * @param MemberDto oldDto
	 * @param String password
	 * @param String email
	 * @param String name
	 * @return DTO에 자료를 담아 return
	 */
	public MemberDto updateDto(HttpServletRequest request, @RequestParam Map params, MemberDto oldDto) {
		MemberDto dto = new MemberDto();
		
		dto.setId(oldDto.getId());
		dto.setUserId(oldDto.getUserId());
		dto.setPassword(request.getParameter("password"));
		dto.setEmail(request.getParameter("email"));
		dto.setName(request.getParameter("name"));
		dto.setLevel(oldDto.getLevel());
		logger.debug("dto >>> {}", dto);
		return dto;
	}
	
	/**
	 * 메일발송 메서드
	 */
	public void mail() {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(adminmail, adminpass);
			}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			
			//발신자 주소
			message.setFrom(new InternetAddress(adminmail));
			
			//수신자 주소
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("hp0932@naver.com"));
			
			//제목
			message.setSubject("제목입니다");
			
			//내용
			message.setText("내용입니다");
			
			Transport.send(message);
			logger.debug("메일 발송 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
