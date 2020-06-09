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

import com.custard.service.FileviewService;

@Controller
@RequestMapping(value = "/fileview",  method = RequestMethod.GET)
public class FileviewController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileviewController.class);
	
	@Autowired
	private FileviewService fileviewService;

	/**
	 * 파일 다운로더 페이지 로드
	 * @param request
	 * @param params
	 * @param map
	 * @return 파일 다운로더 페이지
	 */
	@RequestMapping(value = "/downloader", method = RequestMethod.POST)
	public String goDownloadPage(HttpServletRequest request, @RequestParam Map params, ModelMap map) {
		
		String folder = request.getParameter("folderName");
		String path = request.getParameter("path");
		//폴더값이 없을때 첫로드
		if(folder == null) {
			folder = "";
			logger.debug("fileview first load");
		}else {
			//폴더값이 있다면 폴더값 출력
			logger.debug("select folder >>> {}", folder);
		}
		//path값 널 안정용
		if(path == null) {
			path = "";
		}
		
		Map data = fileviewService.getFileList(path, folder);
		
		map.addAttribute("data", data);
		
		return "fileview/downloader";
	}
	
	//토렌트 업로드 폴더 로드
	@RequestMapping(value = "/upTorrent", method = RequestMethod.POST)
	public String goTorrentPage(HttpServletRequest request, @RequestParam Map params, ModelMap map) {
		
		String folder = request.getParameter("folderName");
		String path = request.getParameter("path");
		if(folder == null) {
			folder = "";
			logger.debug("fileview first load");
		}else {
			logger.debug("select folder >>> {}", folder);
		}
		if(path == null) {
			path = "";
		}
		
		Map data = fileviewService.getTorrentFileList(path, folder);
		
		map.addAttribute("data", data);
		
		return "fileview/downloader";
	}
	
	/**
	 * 파일 업로드
	 * @param request
	 * @param params
	 * @param map
	 * @param session
	 * @param mpfRequest
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String setFile(HttpServletRequest request, @RequestParam Map params,  ModelMap map, HttpSession session, MultipartHttpServletRequest mpfRequest) {
		fileviewService.setFile(mpfRequest.getFiles("attachFile"));
		
		return "redirect:/fileview/downloader";
	}
	
	/**
	 * 폴더 리스트 카운터
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public int getFilesCount(HttpServletRequest request, HttpServletResponse response) {
		String folderName = request.getParameter("folderName");
		int count = fileviewService.getFilesCount(request, response, folderName);
		
		return count;
	}
	
	/**
	 * 폴더 다운로더
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/dirDown", method = RequestMethod.GET)
	public void getFiles(HttpServletRequest request, HttpServletResponse response) {
		String folderName = request.getParameter("folderName");
		String count = request.getParameter("count");
		logger.debug("folder & count >>> {} / {}", folderName, count);
		fileviewService.getFiles(request, response, folderName, Integer.parseInt(count));
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void getFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		fileviewService.getFile(request, response, fileName);
	}
	
	/**
	 * 파일 삭제
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteFile(HttpServletRequest request, HttpServletResponse response) {
		fileviewService.deleteFile(request, response);
		
		return "redirect:/fileview/downloader";
	}
}
