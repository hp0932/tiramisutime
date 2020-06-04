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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custard.controller.FileviewController;
import com.custard.dto.FileDto;

@Service
public class FileviewService {

	@Autowired
	ModelMapper modelMapper;

	private static final Logger logger = LoggerFactory.getLogger(FileviewService.class);
	private static final String ROUTE = "D:\\workshop";
	private static final String TORRENT_ROUTE = "D:\\workshop";

	/**
	 * 파일리스트 출력
	 * 
	 * @return
	 */
	public Map getFileList(String path, String folder) {
		HashMap result = new HashMap();
		String userRoute = ROUTE;
		if (!path.equals("")) {
			userRoute = userRoute + "/" + path;
		}
		userRoute = userRoute + "/" + folder;
		logger.debug("now path >>> {} / now folder >>> {}", path, folder);
		logger.debug("now route >>> {}", userRoute);

		// 파일리스트 및 폴더리스트
		List fileList = new ArrayList();
		List folderList = new ArrayList();

		try {
			for (File info : new File(userRoute).listFiles()) {
				// 폴더리스트
				if (info.isDirectory()) {
					// 루트위치를 포함한 폴더명 제작
					String fullName = userRoute + "/" + info.getName();
					Long fileSize = info.length();
					logger.debug(fullName);
					Map fileInfo = new HashMap();

					// 폴더명, 경로및폴더명, 파일사이즈(폴더이기에 0임), 폴더여부
					fileInfo.put("fileName", info.getName());
					fileInfo.put("fullName", fullName);
					fileInfo.put("fileSize", fileSizeMaker(fileSize));
					fileInfo.put("isFile", "folder");
					folderList.add(fileInfo);
				}
				// 파일리스트
				if (info.isFile()) {
					// 루트위치를 포함한 파일명 제작
					String fullName = userRoute + "/" + info.getName();
					Long fileSize = info.length();
					logger.debug(fullName);
					Map fileInfo = new HashMap();

					// 파일명, 경로및파일명, 파일사이즈, 파일여부
					fileInfo.put("fileName", info.getName());
					fileInfo.put("fullName", fullName);
					fileInfo.put("fileSize", fileSizeMaker(fileSize));
					fileInfo.put("isFile", "file");
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

		result.put("path", path);
		result.put("folder", folder);
		result.put("fileList", fileList);
		result.put("folderList", folderList);
		return result;
	}

	/**
	 * 토렌트 파일리스트 출력
	 * 
	 * @return
	 */
	public Map getTorrentFileList(String path, String folder) {
		HashMap result = new HashMap();
		String userRoute = TORRENT_ROUTE;
		if (!path.equals("")) {
			userRoute = userRoute + "/" + path;
		}
		userRoute = userRoute + "/" + folder;
		logger.debug("now path >>> {} / now folder >>> {}", path, folder);
		logger.debug("now route >>> {}", userRoute);

		// 파일리스트 및 폴더리스트
		List fileList = new ArrayList();
		List folderList = new ArrayList();

		try {
			for (File info : new File(userRoute).listFiles()) {
				// 폴더리스트
				if (info.isDirectory()) {
					// 루트위치를 포함한 폴더명 제작
					String fullName = userRoute + "/" + info.getName();
					Long fileSize = info.length();
					logger.debug(fullName);
					Map fileInfo = new HashMap();

					// 폴더명, 경로및폴더명, 파일사이즈(폴더이기에 0임), 폴더여부
					fileInfo.put("fileName", info.getName());
					fileInfo.put("fullName", fullName);
					fileInfo.put("fileSize", fileSizeMaker(fileSize));
					fileInfo.put("isFile", "folder");
					folderList.add(fileInfo);
				}
				// 파일리스트
				if (info.isFile()) {
					// 루트위치를 포함한 파일명 제작
					String fullName = userRoute + "/" + info.getName();
					Long fileSize = info.length();
					logger.debug(fullName);
					Map fileInfo = new HashMap();

					// 파일명, 경로및파일명, 파일사이즈, 파일여부
					fileInfo.put("fileName", info.getName());
					fileInfo.put("fullName", fullName);
					fileInfo.put("fileSize", fileSizeMaker(fileSize));
					fileInfo.put("isFile", "file");
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

		result.put("path", path);
		result.put("folder", folder);
		result.put("fileList", fileList);
		result.put("folderList", folderList);
		return result;
	}

	/**
	 * 파일 업로드
	 * 
	 * @param fileThread
	 * @param fileUploader
	 * @param files
	 */
	public void setFile(List<MultipartFile> files) {
		String uploadRoute = TORRENT_ROUTE + "/";

		logger.debug(">>> START FILE SAVE <<<");
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			String fileName = file.getOriginalFilename();
			Long fileSize = file.getSize();

			logger.debug("name : {} / size : {}", fileName, fileSize);
			logger.debug("upload route >>> {}", uploadRoute + fileName);
			try {
				File nFile = new File(uploadRoute + fileName);
				file.transferTo(nFile);
				nFile.setExecutable(true, false);
				nFile.setReadable(true, false);
				nFile.setWritable(true, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 파일 다운로드
	 * 
	 * @param request
	 * @param response
	 */
	public void getFile(HttpServletRequest request, HttpServletResponse response) {

		boolean lost = false;
		File file = null;
		InputStream in = null;
		OutputStream os = null;
		String fileName = request.getParameter("fileName");
		String originalName = "";
		logger.debug("select file >>> {}", fileName);

		try {
			try {
				file = new File(ROUTE, fileName);
				in = new FileInputStream(file);
				originalName = file.getName();
			} catch (FileNotFoundException e) {
				logger.debug("§§§ file lost §§§");
				lost = true;
			}
			String client = request.getHeader("User-Agent");
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Description", "JSP Generated Data");

			logger.debug("client >>> {}", client);
			// 파일이 분실되지 않았다면
			if (!lost) {
				// IE
				if (client.indexOf("MSIE") != -1) {
					response.setHeader("Content-Disposition", "attachment; filename="
							+ new String(originalName.getBytes("KSC5601"), "ISO8859_1").replaceAll("\\+", "%20"));

				} else {
					// 한글 파일명 처리
					originalName = new String(originalName.getBytes("UTF-8"), "ISO-8859-1");

					response.setHeader("Content-Disposition", "attachment; filename=\"" + originalName + "\"");
					response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
				}

				response.setHeader("Content-Length", "" + file.length());

				os = response.getOutputStream();
				byte b[] = new byte[8192];
				int leng = 0;
				int limitSize = 0;
				while ((leng = in.read(b)) > 0) {
					os.write(b, 0, leng);
					limitSize += leng;
					if (limitSize > 1024 * 1024) {
						limitSize = 0;
						os.flush();
					}
				}
			}

			in.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 파일 삭제
	 * 
	 * @param request
	 * @param response
	 */
	public void deleteFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		logger.debug("select delete file >>> {}", fileName);
		File file = new File(ROUTE, fileName);

		if (file.exists()) {
			//파일일 경우
			if(file.isFile()) {
				if (file.delete()) {
					logger.debug("파일 삭제");
				} else {
					logger.debug("파일 삭제에 실패하였습니다.");
				}
			}else {
				deleteFolder(ROUTE + "/" + fileName);
			}
		} else {
			logger.debug("파일이 존재하지 않습니다.");
		}
	}
	
	/**
	 * 폴더삭제 재귀함수
	 * @param path
	 */
	public void deleteFolder(String path) {
		logger.debug("select delete folder >>> {}", path);
		File folder = new File(path);
		try {
			if (folder.exists()) {
				//파일리스트를 불러옴
				File[] fileList = folder.listFiles();
				
				//리스트 갯수만큼 파일 삭제
				for (int i = 0; i < fileList.length; i++) {
					//파일 이라면 파일삭제
					if (fileList[i].isFile()) {
						fileList[i].delete();
						logger.debug("하위 파일 삭제");
					} else {
						//폴더일 경우 자기 자신을 다시 호출.
						deleteFolder(fileList[i].getPath());
						logger.debug("하위 폴더 삭제");
					}
				}
				//본래의 폴더 삭제
				folder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 파일 사이즈 처리
	 * 
	 * @param fileSize
	 * @return String 파일+사이즈
	 */
	public String fileSizeMaker(Long fileSize) {
		int sizeChecker = 0;
		String unit = "";
		while (fileSize > 1024) {
			fileSize = fileSize / (long) 1024;
			sizeChecker++;
			if (fileSize < 1024) {
				break;
			} else if (sizeChecker == 5) {
				break;
			}
		}
		// sizecheck의 의미 : 0-byte, 1-kb, 2-mb, 3-gb, 4-tb
		if (sizeChecker == 0) {
			unit = "Byte";
		} else if (sizeChecker == 1) {
			unit = "KB";
		} else if (sizeChecker == 2) {
			unit = "MB";
		} else if (sizeChecker == 3) {
			unit = "GB";
		} else if (sizeChecker == 4) {
			unit = "TB";
		} else {
			unit = "PB";
		}

		return fileSize + unit;
	}
}
