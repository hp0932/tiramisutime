package com.custard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.custard.dto.BoardDto;
import com.custard.service.BoardService;
import com.custard.service.FileBoardService;
import com.custard.service.FileService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/fileBoard",  method = RequestMethod.GET)
public class FileBoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileBoardController.class);
	
	@Autowired
	private FileBoardService boardService;
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 게시판 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "list",  method = RequestMethod.GET)
	public String getBoardList(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		
		Map data = boardService.getBoardList(request, params);
		
		map.addAttribute("data", data);
		
		return "fileBoard/list";
	}
	
	/**
	 * 읽기 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "read",  method = RequestMethod.GET)
	public String getBoardRead(HttpServletRequest request, @RequestParam Map params,  ModelMap map) {
		
		Map data = boardService.getBoardRead(request, params);
		
		map.addAttribute("data", data);
		
		return "fileBoard/read";
	}
	
	/**
	 * 쓰기 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "write",  method = RequestMethod.GET)
	public String getBoardWrite(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		Map data = new HashMap<>();
		
		data.put("userId", session.getAttribute("userId"));
		data.put("name", session.getAttribute("name"));
		
		map.addAttribute("data", data);
		
		return "fileBoard/write";
	}
	
	/**
	 * 수정 페이지 출력
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return jsp page
	 */
	@RequestMapping(value = "modify", method = RequestMethod.GET)
	public String getBoardModify(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		
		Map data = boardService.getBoardRead(request, params);
		
		map.addAttribute("data", data);
		
		return "fileBoard/modify";
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void getFile(HttpServletRequest request, HttpServletResponse response) {
		fileService.getFile(request, response);
	}
	
	@RequestMapping(value = "fileDelete", method = RequestMethod.GET)
	public void fileDelete(HttpServletRequest request, HttpServletResponse response) {
		fileService.fileDelete(Long.parseLong(request.getParameter("fileId")));
	}
	
	/**
	 * 게시글 저장
	 * @param request
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "commit",  method = RequestMethod.POST)
	public String setBoard(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session, MultipartHttpServletRequest mpfRequest) {
		
		Long data = boardService.setBoardWrite(request, params, session);
		fileService.setFile(data, session.getAttribute("userId").toString(), mpfRequest.getFiles("attachFile"));
		
		return "redirect:/fileBoard/list";
	}
	
	/**
	 * 게시글 업데이트
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String setBoardUpdate(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session, MultipartHttpServletRequest mpfRequest) {
		
		Long data = boardService.setBoardUpdate(request, params, session);
		fileService.setFile(data, session.getAttribute("userId").toString(), mpfRequest.getFiles("attachFile"));
		
		return "redirect:/fileBoard/list";
	}
	
	/**
	 * 게시글 삭제
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String setBoardDelete(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session) {
		
		boardService.setBoardDelete(request, params, session);
		fileService.filesDelete(Long.parseLong(request.getParameter("nowThread")));
		
		return "redirect:/fileBoard/list";
	}
}
