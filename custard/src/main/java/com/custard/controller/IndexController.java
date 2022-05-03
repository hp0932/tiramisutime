package com.custard.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Value("${first}")
	private String first;

	/**
	 * welcome페이지 출력
	 * @param request
	 * @param params
	 * @return index페이지
	 */
	@RequestMapping(value = "/",  method = RequestMethod.GET)
	public String goIndex(HttpServletRequest request, @RequestParam Map params, ModelMap map) {

		logger.debug(first);
		return "index";
	}
	
	/**
	 * 사용기술 페이지 출력
	 * @param request
	 * @param params
	 * @return 사용기술 페이지
	 */
	@RequestMapping(value = "/tech", method = RequestMethod.GET)
	public String goTech(HttpServletRequest request, @RequestParam Map params) {
		return "common/tech";
	}
	
	/**
	 * 어드민 페이지 이미지 출력
	 * @param request
	 * @param params
	 * @return 관리자페이지 이미지
	 */
	@RequestMapping(value = "/adminImg", method = RequestMethod.GET)
	public String goAdminImg(HttpServletRequest request, @RequestParam Map params) {
		return "common/adminimg";
	}
	
}
