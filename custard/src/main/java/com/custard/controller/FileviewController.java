package com.custard.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.custard.service.FileviewService;

@Controller
public class FileviewController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileviewController.class);
	
	@Autowired
	private FileviewService torrentService;

	@RequestMapping(value = "/downloader", method = RequestMethod.GET)
	public String goDownloadPage(HttpServletRequest request, @RequestParam Map params, ModelMap map) {
		
		Map data = torrentService.getFileList();
		
		map.addAttribute("data", data);
		
		return "fileview/downloader";
	}

}
