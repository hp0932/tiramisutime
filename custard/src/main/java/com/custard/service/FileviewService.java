package com.custard.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.custard.controller.FileviewController;

@Service
public class FileviewService {
	
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
