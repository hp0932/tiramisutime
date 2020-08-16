package com.custard.controller;

import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.custard.service.FileShareService;

@Controller
@RequestMapping(value = "/fileshare",  method = RequestMethod.GET)
public class FileShareController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileShareController.class);
	
	@Autowired
	private FileShareService fileshareService;

	/**
	 * 파일 다운로더 페이지 로드
	 * @param request
	 * @param params
	 * @param map
	 * @return 파일 다운로더 페이지
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public String goDownloadPage(HttpServletRequest request, @RequestParam Map params, ModelMap map, HttpSession session) {
		
		if(session.getAttribute("level") == null || session.getAttribute("level").equals("1")) {
			logger.debug("user level >>> {}", session.getAttribute("level"));
			return "redirect:/";
		}else {
			String folder = request.getParameter("folderName");
			String path = request.getParameter("path");
			//폴더값이 없을때 첫로드
			if(folder == null) {
				folder = "";
				logger.debug("fileshare first load");
			}else {
				//폴더값이 있다면 폴더값 출력
				logger.debug("select folder >>> {}", folder);
			}
			//path값 널 안정용
			if(path == null) {
				path = "";
			}
			
			Map data = fileshareService.getFileList(path, folder);
			
			map.addAttribute("data", data);
			
			return "fileShare/share";
		}
	}
	
	
	/**
	 * 파일 업로드
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @param mpfRequest
	 * @return 파일 다운로더 페이지
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String setFile(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session, MultipartHttpServletRequest mpfRequest) {
		fileshareService.setFile(mpfRequest.getFiles("attachFile"));
		
		return "redirect:/fileShare/share";
	}
	
	/**
	 * 폴더 리스트 카운터
	 * @param request
	 * @param response
	 * @return 폴더 내 파일 갯수
	 */
	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public int getFilesCount(HttpServletRequest request, HttpServletResponse response) {
		String folderName = request.getParameter("folderName");
		int count = fileshareService.getFilesCount(request, response, folderName);
		
		return count;
	}
	
	/**
	 * 폴더 다운로더
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/dirDown", method = RequestMethod.POST)
	public void getFiles(HttpServletRequest request, HttpServletResponse response) {
		String folderName = request.getParameter("folderName");
		String count = request.getParameter("count");
		
		//폴더명과 현재 다운로드 count를 넣어서 다중다운로드 실행
		logger.debug("folder & count >>> {} / {}", folderName, count);
		fileshareService.getFiles(request, response, folderName, Integer.parseInt(count));
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void getFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		fileshareService.getFile(request, response, fileName);
	}
	
	/**
	 * 파일 삭제
	 * @param request
	 * @param response
	 * @return 파일 다운로더 페이지
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteFile(HttpServletRequest request, HttpServletResponse response) {
		fileshareService.deleteFile(request, response);
		
		return "redirect:/fileShare/share";
	}
}
