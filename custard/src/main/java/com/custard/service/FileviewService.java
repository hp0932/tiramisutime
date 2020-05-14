package com.custard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.custard.controller.FileviewController;
import com.custard.dto.FileDto;

@Service
public class FileviewService {
	
	@Autowired
	ModelMapper modelMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(FileviewService.class);
	private static final String ROUTE = "D:\\workshop";
	
	public Map getFileList() {
		HashMap result = new HashMap();
		
		List fileList = new ArrayList();
		
		try {
			for (File info : new File(ROUTE).listFiles()) {
				if (info.isDirectory()) {
					logger.debug("dir : {}", info.getName());
				}
				if (info.isFile()) {
					String fullName = ROUTE + "\\" + info.getName();
					Long fileSize = info.length();
					logger.debug(fullName);
					Map fileInfo = new HashMap();
					fileInfo.put("fileName", info.getName());
					fileInfo.put("fullName", fullName);
					fileInfo.put("fileSize", fileSizeMaker(fileSize));
					fileList.add(fileInfo);
				}
			}
		} catch (Exception e) {
			logger.debug("하위 디렉토리 및 파일이 없습니다");
		}

		/*
		 * // 하위의 모든 파일 for (File info : FileUtils.listFiles(new File(ROUTE),
		 * TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
		 * System.out.println(info.getName()); }
		 */
        
		result.put("fileList", fileList);
		return result;
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 */
	public void getFile(HttpServletRequest request, HttpServletResponse response) {
		
		boolean lost = false;
		File file = null;
		InputStream in = null;
		OutputStream os = null;
		String fileName = request.getParameter("fileName");
		
		try {
			try {
				file = new File(ROUTE, fileName);
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.debug("§§§ file lost §§§");
				lost = true;
			}
			String client = request.getHeader("User-Agent");
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Description", "JSP Generated Data");
			
			//파일이 분실되지 않았다면
			if(!lost) {
				// IE
				if(client.indexOf("MSIE") != -1){
					response.setHeader ("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("KSC5601"),"ISO8859_1"));
					
				}else{
					// 한글 파일명 처리
					fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
					
					response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
				} 
				
				response.setHeader ("Content-Length", ""+file.length() );
				
				
				
				os = response.getOutputStream();
				byte b[] = new byte[8192];
				int leng = 0;
				int limitSize = 0;
				while( (leng = in.read(b)) > 0 ){
					os.write(b,0,leng);
					limitSize += leng;
					if(limitSize > 1024 * 1024) {
						limitSize = 0;
						os.flush();
					}
				}
			}
			
		in.close();
		os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String fileSizeMaker(Long fileSize) {
		int sizeChecker = 0;
		String unit = "";
		while(fileSize > 1024) {
			fileSize = fileSize/(long) 1024;
			sizeChecker++;
			if(fileSize <  1024) {
				break;
			}else if (sizeChecker == 5) {
				break;
			}
		}
		//sizecheck의 의미 : 0-byte, 1-kb, 2-mb, 3-gb, 4-tb
		if(sizeChecker == 0) {
			unit = "Byte";
		}else if (sizeChecker == 1) {
			unit = "KB";
		}else if (sizeChecker == 2) {
			unit = "MB";
		}else if (sizeChecker == 3) {
			unit = "GB";
		}else if (sizeChecker == 4) {
			unit = "TB";
		}else {
			unit = "PB";
		}
		
		return fileSize + unit;
	}
}
