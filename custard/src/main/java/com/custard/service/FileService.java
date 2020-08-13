package com.custard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.custard.dto.BoardDto;
import com.custard.dto.FileDto;
import com.custard.entity.FileEntity;
import com.custard.repository.FileRepository;

@Service
public class FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	//private static final String ROUTE = "D:\\";
	private static final String ROUTE = "/usr/files/";
	
	@Autowired
	FileRepository fileRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * 파일 업로드
	 * @param fileThread
	 * @param fileUploader
	 * @param files
	 */
	public void setFile(Long fileThread, String fileUploader, List<MultipartFile> files) {
		FileDto dto = new FileDto();
		
		logger.debug(">>> START FILE SAVE <<<");
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			String fileName = file.getOriginalFilename();
			Long fileSize = file.getSize();
			String saveName = UUID.randomUUID().toString();
			logger.debug("name : {} / size : {}", fileName, fileSize);
			logger.debug("file save name : {}", saveName);
			
			//파일정보 DB 저장
			fileRepo.save(putSaveDto(fileThread, fileName, saveName, fileSize, fileUploader).toEntity());
			try {
				file.transferTo(new File(ROUTE + saveName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 */
	public void getFile(HttpServletRequest request, HttpServletResponse response) {
		FileDto dto = modelMapper.map(fileRepo.getOne(Long.parseLong(request.getParameter("fileId"))), FileDto.class);
		
		boolean lost = false;
		File file = null;
		InputStream in = null;
		OutputStream os = null;
		String saveName = dto.getSaveName();
		String fileName = dto.getFileName();
		
		try {
			try {
				file = new File(ROUTE, saveName);
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
	
	/**
	 * 파일 삭제
	 * @param fileId
	 */
	public void fileDelete(Long fileId) {
		FileDto dto = modelMapper.map(fileRepo.getOne(fileId), FileDto.class);
		fileRepo.deleteById(fileId);
		
		File file = new File(ROUTE + dto.getSaveName());
		if(file.exists()) {
			if(file.delete()) {
				logger.debug("");
			}else {
				logger.debug("파일 삭제에 실패하였습니다.");
			}
		}else {
			logger.debug("파일이 존재하지 않습니다.");
		}
	}
	
	/**
	 * 게시글 삭제시 파일 전체삭제
	 * @param threadId
	 */
	public void filesDelete(Long threadId) {
		List<FileEntity> fileList = fileRepo.findByFileThread(threadId);
		
		for (int i = 0; i < fileList.size(); i++) {
			fileDelete(fileList.get(i).getId());
		}
	}
	
	/**
	 * 파일 업로드용 dto처리
	 * @param fileThread
	 * @param fileName
	 * @param saveName
	 * @param fileSize
	 * @param fileUploader
	 * @return fileDto
	 */
	public FileDto putSaveDto(Long fileThread, String fileName, String saveName, Long fileSize, String fileUploader) {
		FileDto dto = new FileDto();
		dto.setFileThread(fileThread);
		dto.setFileName(fileName);
		dto.setSaveName(saveName);
		dto.setFileSize(fileSize);
		dto.setFileUploader(fileUploader);
		
		return dto;
	}
}
