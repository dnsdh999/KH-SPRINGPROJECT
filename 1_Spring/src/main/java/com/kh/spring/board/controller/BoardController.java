package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagenation;
import com.kh.spring.member.model.vo.Member;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;
	
	@RequestMapping("blist.bo")
	public /*String*/ ModelAndView boardList(@RequestParam(value="page", required=false) Integer page, /*Model model*/ModelAndView mv) {
		
		
		
		//int로도 가져올 수 있음
		int currentPage = 1;
		//계속 1일 수는 없음. 변경이 되어야 할때는 ?
		//currentPage가 null이 아니면 .. 이라고 두어야함.
		//0이 아니다라는 조건은 사용이 불가.
		//페이지가 넘어왔다 안넘어왔다?? -> 이를 체크해야하기때문에
		//int -> Integer로 변경하면 wrapper class이기 때문에 null값을 조건식에 사용할 수 있음.
		if(page != null) {
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
		PageInfo pi = Pagenation.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if(list != null) {
			// 1.반환값이 String 인 상태에서 view에 데이터 전달 -> Model 파라미터 추가 필요
			//model.addAttribute("pi", pi);
			//model.addAttribute("list", list);
			
			// 2. 반환값이 ModelAndView로 변경한 상태에서 view로 데이터 전달
			mv.addObject("pi", pi);
			mv.addObject("list", list);
			mv.setViewName("boardListView");
		} else {
			throw new BoardException("게시판 전체 조회에 실해하였어요");
		}
		
		//스트링일 시 아래를 포함한 주석 체크 풀고 알아서 잘!
		//return "boardListView";
		return mv;
	}
	
	@RequestMapping("binsertView.bo")
	public String bInsertForm() {
		return "boardInsertForm";
	}
	
	@RequestMapping("binsert.bo")
	public String boardInsert(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {	//라이브러리 필요, pom.xml에 관련 코드 추가
		System.out.println(b);
		System.out.println(uploadFile);
		if(!uploadFile.getOriginalFilename().equals("")) {//파일이 들어왔는지 확인하고 싶으면, 이 방식으로 하는게 맞ㅈ음.
		//if(uploadFile != null && !uploadFile.isEmpty()){	//이렇게도 가능
			String renameFileName = saveFile(uploadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(uploadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:blist.bo";
		}else {
			throw new BoardException("게시판 삽입에 실해하였어요");
		}
		
		
	}

	private String saveFile(MultipartFile file, HttpServletRequest request) {
		//이름 바꾸는거, 어디에다 저장을 할지
		//작은 resources에 접근할 것임
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/buploadFiles";
		
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String originFileName = file.getOriginalFilename();
		String renameFileName
			= sdf.format(new Date(System.currentTimeMillis())) + "." + originFileName.substring(originFileName.lastIndexOf(".") + 1);
		
		String renamePath = folder + "/" + renameFileName;
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return renameFileName;
	}
	
	@RequestMapping("bdetail.bo")
	public String boardDetailView(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model) {
		int result = bService.viewCountUp(bId);
		if(result==0) {
			throw new BoardException("게시글 조회에 실해하였어요");
		}
		Board b = bService.selectBoard(bId);
		if(b != null) {
			model.addAttribute("board", b);
			model.addAttribute("page", page);
		} else {
			throw new BoardException("게시글 조회에 실해하였어요");
		}
		
		return "boardDetailView";

	}
	
	@RequestMapping("bdetaill.bo")
	public String selectBoard(@RequestParam("bId") int bId, Model model){
		Board b = bService.selectBoard(bId);
		if(b != null) {
			model.addAttribute("board", b);
			return "boardDetail";
		}else {
			return "redirect:error.do";
		}
	}
	
	/*
	@RequestMapping("bdetail.bo")
	public String boardDetailView(@RequestParam("bId") int bId, @RequestParam("page") int page, ModelAndView mv) {
		int result = bService.viewCountUp(bId);
		if(result==0) {
			throw new BoardException("게시글 조회에 실해하였어요");
		}
		Board b = bService.selectBoard(bId);
		if(b != null) {
			mv.addObject("board",b);
			mv.addObject("page",page);
			mv.setViewName("boardDetailView");
		} else {
			throw new BoardException("게시글 조회에 실해하였어요");
		}
		
		return mv;

	}*/
	
	@RequestMapping("bupView.bo")
	public String boardUpdateView(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model) {
		Board b = bService.selectBoard(bId);
		if(b != null) {
			model.addAttribute("board", b);
			model.addAttribute("page", page);
		} else {
			throw new BoardException("게시글수정페이지조회에 실해하였어요");
		}
		return "boardUpdateForm";
	}
	
	@RequestMapping("bupdate.bo")
	public String updateBoard(@ModelAttribute Board b, @RequestParam("reloadFile") MultipartFile reloadFile, @RequestParam("page") int page, HttpServletRequest request, Model model) {
		System.out.println(reloadFile);
		
		if(reloadFile != null && !reloadFile.isEmpty()) {	//수정할 파일이 존재한다.
			//수정할 파일 + 기존 파일도 존재 = 기존 파일을 삭제해야한다는 이야기
			if(b.getRenameFileName()!=null) {	//기존 파일이 존재한다면
				deleteFile(b.getRenameFileName(), request);
			}
			
			String renameFileName = saveFile(reloadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(reloadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			model.addAttribute("page",page);
		}else {
			throw new BoardException("게시글수정에 실패해하였어요");
		}
		
		return "redirect:bdetail.bo?bId=" + b.getBoardId();
	}
	
	
	public void deleteFile(String fileName, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/buploadFiles";
		
		File f = new File(savePath + "/" + fileName);
		
		if(f.exists()) {
			f.delete();
		}
	}
	
	
	@RequestMapping("bdelete.bo")
	public String boardDelete(@RequestParam("bId") int bId, Model model, HttpServletRequest request) {
		/*Board b = bService.selectBoard(bId);
		if(b.getOriginalFileName() != null) {
			deleteFile(b.getRenameFileName(), request);
		}*/
		
		int result = bService.deleteBoard(bId);
		if(result == 0)  {
			throw new BoardException("게시글수정페이지조회에 실해하였어요");
		}
		return  "redirect:blist.bo";
	}
	
	@RequestMapping("addReply.bo")
	@ResponseBody
	public String addReply(@ModelAttribute Reply r, HttpServletRequest request) {
		//반환 값 String
		//댓글등록 완료 시 success가 되도록
		
		String rWriter = ((Member)request.getSession().getAttribute("loginUser")).getId();
	      r.setReplyWriter(rWriter);
		
		int result = bService.addReply(r);
		
		if(result > 0) {
			return "success";
		}else{
			throw new BoardException("댓글 등록 실패");
		}
	}
	
//	@RequestMapping(value="rList.bo", produces="application/json; charset=UTF-8")
//	@ResponseBody
	@RequestMapping("rList.bo")
//	public String replyList(@RequestParam("boardId") int boardId) {
	public void getReplyList(@RequestParam("boardId") int boardId, HttpServletResponse response) {
		ArrayList<Reply> list = bService.selectReplyList(boardId);
		
//		if(list != null) {
//			JSONArray jArr = new JSONArray();
//			JSONObject userObj = null;
//			for(Reply r : list) {
//				userObj = new JSONObject();
//				
//				userObj.put("replyId", r.getReplyId());
//				userObj.put("replyContent", r.getReplyContent());
//				userObj.put("refBoardId", r.getRefBoardId());
//				userObj.put("replyWriter", r.getReplyWriter());
//				userObj.put("nickName", r.getNickName());
//				userObj.put("replyCreateDate", r.getReplyCreateDate().toString());	//json자체가 date형식을 받을 수가 없다.
//				userObj.put("replyModifyDate", r.getReplyModifyDate().toString());
//				userObj.put("replyStatus", r.getReplyStatus());
//				jArr.add(userObj);
//			}
//			return jArr.toJSONString();

		if(list!= null) {
			response.setContentType("application/json; charset=UTF-8");
			
			GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
			
			//Gson gson = new Gson();
			Gson gson = gb.create();
			try {
				gson.toJson(list, response.getWriter());
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			throw new BoardException("댓글 불러오기 실패");
		}
	}
	
	@RequestMapping(value="topList.bo", produces="application/json; charset=UTF-8")
	@ResponseBody
	public void topList(HttpServletResponse response) {
		ArrayList<Board> list = bService.selectTopN();
		if(list!= null) {
			response.setContentType("application/json; charset=UTF-8");
			
			GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");

			Gson gson = gb.create();
			try {
				gson.toJson(list, response.getWriter());
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			throw new BoardException("탑엔 불러오기 실패");
		}
	}
}
