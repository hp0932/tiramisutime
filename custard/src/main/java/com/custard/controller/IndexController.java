package com.custard.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping(value = "/",  method = RequestMethod.GET)
	public String goIndex(HttpServletRequest request, @RequestParam Map params) {
		logger.debug("indexController first loaded");
		return "index";
	}
	
	@RequestMapping(value = "/tech", method = RequestMethod.GET)
	public String goTech(HttpServletRequest request, @RequestParam Map params) {
		return "common/tech";
	}
	
	@RequestMapping(value = "/adminImg", method = RequestMethod.GET)
	public String goAdminImg(HttpServletRequest request, @RequestParam Map params) {
		return "common/adminimg";
	}
	
}
