package com.custard.controller;

import java.util.HashMap;
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

import com.custard.dto.BoardDto;
import com.custard.service.BoardService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/board",  method = RequestMethod.GET)
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	/**
	 * 게시판 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return 게시판 목록 페이지
	 */
	@RequestMapping(value = "list",  method = RequestMethod.GET)
	public String getBoardList(HttpServletRequest request, @RequestParam Map params, ModelMap map) {
		
		Map data = boardService.getBoardList(request, params);
		
		map.addAttribute("data", data);
		
		return "board/list";
	}
	
	/**
	 * 읽기 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return 게시글 페이지
	 */
	@RequestMapping(value = "read",  method = RequestMethod.GET)
	public String getBoardRead(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		
		Map data = boardService.getBoardRead(request, params);
		
		map.addAttribute("data", data);
		
		return "board/read";
	}
	
	/**
	 * 쓰기 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return 게시글 쓰기 페이지
	 */
	@RequestMapping(value = "write",  method = RequestMethod.GET)
	public String getBoardWrite(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		Map data = new HashMap<>();
		
		data.put("userId", session.getAttribute("userId"));
		data.put("name", session.getAttribute("name"));
		
		map.addAttribute("data", data);
		
		return "board/write";
	}
	
	/**
	 * 수정 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return 게시글 수정 페이지
	 */
	@RequestMapping(value = "modify", method = RequestMethod.GET)
	public String getBoardModify(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		
		Map data = boardService.getBoardRead(request, params);
		
		map.addAttribute("data", data);
		
		return "board/modify";
	}
	
	/**
	 * 게시글 저장
	 * @param request
	 * @param params
	 * @param map
	 * @return 게시판 리스트 페이지
	 */
	@RequestMapping(value = "commit",  method = RequestMethod.POST)
	public String setBoard(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		Long data = boardService.setBoardWrite(request, params, session);
		
		return "redirect:/board/list";
	}
	
	/**
	 * 게시글 업데이트
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return 게시판 리스트 페이지
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String setBoardUpdate(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		Long data = boardService.setBoardUpdate(request, params, session);
		
		return "redirect:/board/list";
	}
	
	/**
	 * 게시글 삭제
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return 게시판 리스트 페이지
	 */
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String setBoardDelete(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		
		boardService.setBoardDelete(request, params, session);
		
		return "redirect:/board/list";
	}
}
