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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custard.dto.BoardDto;
import com.custard.service.BoardService;
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
	 * @return
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
	 * 로그인
	 * @param request
	 * @param params
	 * @param map
	 * @return 
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
	 * @return
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
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "admin", method = RequestMethod.POST)
	public String getMemberList(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) throws Exception {
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
	 * @return
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
	
	@RequestMapping(value = "admin/userCommit")
	public String setUserLevel(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		logger.debug("now id >>> {}", Long.parseLong(params.get("id").toString()));
		memberService.setUserLevel(Long.parseLong(params.get("id").toString()), Integer.parseInt(params.get("level").toString()));
		
		return "redirect:/member/admin";
	}
}
