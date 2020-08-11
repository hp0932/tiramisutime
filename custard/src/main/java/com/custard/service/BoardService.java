package com.custard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.custard.dto.BoardDto;
import com.custard.entity.BoardEntity;
import com.custard.repository.BoardRepository;

@Service
public class BoardService {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
	
	private static final int PAGE_SIZE = 10;
	
	@Autowired
	BoardRepository boardRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * 게시판 목록조회
	 * @param String searchContent
	 * @param int nowPage
	 * @return Map(Pageable)
	 */
	public Map getBoardList(HttpServletRequest request, @RequestParam Map params) {
		HashMap result = new HashMap();
		List content = new ArrayList<>();
		
		//검색어가 존재할 경우
		if(params.get("searchContent") != null) {
			HashMap boardList = new HashMap();
			
			//검색어 준비
			String search = params.get("searchContent").toString();
			search = "%" + search + "%";
			
			//검색쿼리 실행
			logger.debug("search content >>> {}", search);
			content = boardRepo.findByTitleLikeOrderByIdDesc(search);
			logger.debug("seacrh list >>> {}", content);
			
			//페이징 처리
			int count = content.size();
			int pageCount = count/PAGE_SIZE;
			int nowPage = request.getParameter("nowPage") == null ? 1 : Integer.parseInt(request.getParameter("nowPage").toString());
			int indexCount = nowPage/PAGE_SIZE;
			if(nowPage%PAGE_SIZE == 0) {
				indexCount--;
			}
			int startPage = indexCount*PAGE_SIZE+1;
			int endPage = startPage+PAGE_SIZE-1;
			
			
			
			if(count%PAGE_SIZE != 0) pageCount++;
			if(endPage > pageCount) {
				endPage = pageCount;
			}
			logger.debug("현재 페이지        >>> {}", nowPage);
			logger.debug("글 갯수            >>> {}", count);
			logger.debug("페이지 갯수        >>> {}", pageCount);
			logger.debug("페이지 십의 자릿수 >>> {}", indexCount);
			
			//페이징에 필요한 갯수만큼만 집어넣는 기능
			List searchContent = new ArrayList<>();
			int listStart = (nowPage-1)*PAGE_SIZE;
			int listEnd = (nowPage-1)*PAGE_SIZE+9;
			logger.debug("start page, end page {} : {}", listStart, listEnd);
			for (int i = 0; i < content.size(); i++) {
				logger.debug("content {} >>> {}", i, content.get(i));
				if(i >= listStart && i <= listEnd) {
					logger.debug("add content");
					searchContent.add(content.get(i));
				}
			}
			boardList.put("content", searchContent);
			
			result.put("nowPage", nowPage);
			result.put("boardCount", count);
			result.put("startPage", startPage);
			result.put("endPage", endPage);
			result.put("boardListCount", pageCount);
			result.put("boardList", boardList);
			
		}else {
			//검색어가 없을 경우
			int count = (int) boardRepo.count();
			int pageCount = count/PAGE_SIZE;
			int nowPage = request.getParameter("nowPage") == null ? 1 : Integer.parseInt(request.getParameter("nowPage").toString());
			int indexCount = nowPage/PAGE_SIZE;
			if(nowPage%PAGE_SIZE == 0) {
				indexCount--;
			}
			int startPage = indexCount*PAGE_SIZE+1;
			int endPage = startPage+PAGE_SIZE-1;
			
			//특정 페이지 페이저블 작성
			Pageable pageRequest = PageRequest.of(nowPage-1, PAGE_SIZE, Sort.by("id").descending());
			//해당 페이지를 불러오는 기능
			Page<BoardEntity> page = boardRepo.findAll(pageRequest);
			logger.debug("now Page is >>> {}", request.getParameter("nowPage"));
			
			if(count%PAGE_SIZE != 0) pageCount++;
			if(endPage > pageCount) {
				endPage = pageCount;
			}
			logger.debug("현재 페이지        >>> {}", nowPage);
			logger.debug("글 갯수            >>> {}", count);
			logger.debug("페이지 갯수        >>> {}", pageCount);
			logger.debug("페이지 십의 자릿수 >>> {}", indexCount);
			
			result.put("nowPage", nowPage);
			result.put("boardCount", count);
			result.put("startPage", startPage);
			result.put("endPage", endPage);
			result.put("boardListCount", pageCount);
			result.put("boardList", page);
		}
		
		return result;
	}

	
	/**
	 * 게시판 단건조회
	 * @param String nowThread
	 * @return Map(boardDto)
	 */
	public Map getBoardRead(HttpServletRequest request, @RequestParam Map params) {
		HashMap result = new HashMap();
		
		Long selectThread = (long) Integer.parseInt(request.getParameter("nowThread").toString());
		
		BoardEntity boardEntity = boardRepo.getOne(selectThread);
		BoardDto dto = modelMapper.map(boardEntity, BoardDto.class);
		
		
		result.put("id", dto.getId());
		result.put("title", dto.getTitle());
		result.put("ownerId", dto.getOwnerId());
		result.put("ownerName", dto.getOwnerName());
		result.put("readCnt", dto.getReadCnt());
		result.put("content", dto.getContent());
		result.put("createDate", boardEntity.getCreateDate());
		
		//읽은 횟수 1 증가
		int readCnt = dto.getReadCnt();
		readCnt++;
		dto.setReadCnt(readCnt);
		boardRepo.save(dto.toEntity());
		
		return result;
	}
	
	/**
	 * 게시글 저장
	 * @param boardDto
	 * @param session(userId, name)
	 * @return int threadNo(글번호)
	 */
	@Transactional
	public Long setBoardWrite(HttpServletRequest request, @RequestParam Map params,  HttpSession session) {
		
		BoardDto dto = putWriteDto(request, params);
		dto.setOwnerId(session.getAttribute("userId").toString());
		dto.setOwnerName(session.getAttribute("name").toString());
		logger.debug("write board dto >>> {}", dto);
		
		return boardRepo.save(dto.toEntity()).getId();
	}
	
	/**
	 * 게시글 업데이트
	 * @param boardDto
	 * @param session(userId, name)
	 * @return int threadNo(글번호)
	 */
	@Transactional
	public Long setBoardUpdate(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		
		BoardDto dto = putUpdateDto(request, params);
		dto.setOwnerId(session.getAttribute("userId").toString());
		dto.setOwnerName(session.getAttribute("name").toString());
		logger.debug("update board dto >>> {}", dto);
		
		return boardRepo.save(dto.toEntity()).getId();
	}
	
	/**
	 * 게시글 삭제
	 * @param int nowThread
	 */
	public void setBoardDelete(HttpServletRequest request, @RequestParam Map params, HttpSession session) {
		
		Long boardId = Long.parseLong(request.getParameter("nowThread"));
		
		logger.debug("delete board id >>> {}", boardId);
		
		boardRepo.deleteById(boardId);
	}
	
	/**
	 * 게시글 저장을 위한 dto포장
	 * @param request
	 * @param params
	 * @return boardDto
	 */
	public BoardDto putWriteDto(HttpServletRequest request, @RequestParam Map params) {
		BoardDto dto = new BoardDto();
		
		dto.setTitle(request.getParameter("title"));
		dto.setOwnerId(request.getParameter("ownerId"));
		dto.setOwnerName(request.getParameter("ownerName"));
		dto.setContent(request.getParameter("content"));
		dto.setReadCnt(Integer.parseInt(request.getParameter("readCnt").toString()));
		
		return dto;
	}
	
	/**
	 * 게시글 업데이트를 위한 dto포장
	 * @param request
	 * @param params
	 * @return boardDto
	 */
	public BoardDto putUpdateDto(HttpServletRequest request, @RequestParam Map params) {
		BoardDto dto = new BoardDto();
		
		dto.setId(Long.parseLong(request.getParameter("id")));
		dto.setTitle(request.getParameter("title"));
		dto.setOwnerId(request.getParameter("ownerId"));
		dto.setOwnerName(request.getParameter("ownerName"));
		dto.setContent(request.getParameter("content"));
		dto.setReadCnt(Integer.parseInt(request.getParameter("readCnt").toString()));
		
		return dto;
	}
}
