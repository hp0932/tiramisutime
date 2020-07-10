package com.custard.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custard.service.MemberService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/member",  method = RequestMethod.GET)
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	/**
	 * 회원가입 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return 회원가입 페이지
	 */
	@RequestMapping(value = "join",  method = RequestMethod.GET)
	public String getMemberJoin(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		return "member/join";
	}
	
	/**
	 * 회원가입
	 * @param request
	 * @param params
	 * @param map
	 * @return Long id
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "commit",  method = RequestMethod.POST)
	public Long setMemberJoin(HttpServletRequest request, @RequestParam Map params,  ModelMap map) throws Exception {
		
		Long result = memberService.setMemberJoin(request, params);
		
		return result;
	}
	
	/**
	 * 회원정보변경 페이지 로드
	 * @param String password
	 * @param session String userId
	 * @return 회원정보변경 페이지 || index페이지 리턴
	 */
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String getMemberUpdate(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		
		int test = memberService.getPassTest(request, params, session);
		
		if(test == 1) {
			return "";
		}else {
			return "redirect:/";
		}
	}
	
	/**
	 * 회원정보변경
	 * @param String userId, String password, String email, int level
	 * @param session String userId
	 * @return Long id || -1: 사용자 아이디 오류, -2: 닉네임 중복
	 */
	@RequestMapping(value = "update", method = RequestMethod.PATCH)
	public Long setMemberUpdate(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		return memberService.setMemberUpdate(request, params, session);
	}
	
	/**
	 * 로그인
	 * @param request
	 * @param params
	 * @param map
	 * @return int level
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "login",  method = RequestMethod.POST)
	public int getLogin(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session, RedirectAttributes redirect) throws Exception {
		int level = memberService.getLogin(request, params, session);
		redirect.addAttribute("loginStatus", level);
		return level;
	}
	
	/**
	 * 로그아웃
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return index페이지
	 * @throws Exception
	 */
	@RequestMapping(value = "logout",  method = RequestMethod.POST)
	public String getLogout(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) throws Exception {
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	/**
	 * 관리자페이지 멤버 목록 조회
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return 관리자페이지 || index페이지
	 * @throws Exception
	 */
	@RequestMapping(value = "admin", method = RequestMethod.POST)
	public String getMemberList(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) throws Exception {
		

		//어드민 페이지 level null처리
		if(session.getAttribute("level") == null) {
			return "redirect:/";
		}
		
		int level = (int) session.getAttribute("level");
		
		Map data = memberService.getMemberList(request, params, session);
		Map levelList = memberService.getLevelList(request, params, session);
		
		map.addAttribute("levelList", levelList);
		map.addAttribute("data", data);
		
		//최고권한 관리자일 경우에만
		if(level == 2) {
			return "common/admin";
		}else {
			return "redirect:/";
		}
	}
	
	/**
	 * 레벨 저장
	 * @param request
	 * @param params
	 * @param map
	 * @return 관리자페이지
	 */
	@RequestMapping(value = "admin/levelCommit", method = RequestMethod.GET)
	public String setLevel(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		
		if(params.get("id") != null) {
			memberService.setLevel(Long.parseLong(params.get("id").toString()), Integer.parseInt(params.get("level").toString()), params.get("levelName").toString());
		}else {
			memberService.setLevel(Integer.parseInt(params.get("level").toString()), params.get("levelName").toString());
		}
		logger.debug("level save");
		
		return "redirect:/member/admin";
	}
	
	/**
	 * 유저 레벨 변경
	 * @param request
	 * @param params
	 * @param map
	 * @return 관리자페이지
	 */
	@RequestMapping(value = "admin/userCommit")
	public String setUserLevel(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		logger.debug("now id >>> {}", Long.parseLong(params.get("id").toString()));
		memberService.setUserLevel(Long.parseLong(params.get("id").toString()), Integer.parseInt(params.get("level").toString()));
		
		return "redirect:/member/admin";
	}
}
