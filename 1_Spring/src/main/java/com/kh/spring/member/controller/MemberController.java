package com.kh.spring.member.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.log.Log4jTest;
import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;


@SessionAttributes("loginUser")
@Controller	//컨트롤러 어노테이션
public class MemberController {
	
	@Autowired	//오토와이어드 사용으로 서비스 객체 생성 후 매칭되지 않았다면 이것으로 해결가능
	private MemberService mService; //인터페이스로 불러온다. 이런 경우 서비스클래스 이름을 바꾼다고 해서 문제가 되지 않는다 (영향을 받지않게된다.)
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);

//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login() {
//		System.out.println("로그인 처리합니다.");
//	}
	
	/********* 파라미터 전송받기 ******************/
	//1. HttpServletRequest를 통해 전송받기 - JSP/SERVLET방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		String pwd = request.getParameter("pwd");
//		
//		System.out.println("id1 : " + id);
//		System.out.println("pwd1 : " + pwd);
//	}
	
	//2.@RequestParam 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@RequestParam(value="id", defaultValue="hello") String userId,
//					  @RequestParam(value="pwd", defaultValue="world") String userPwd,
//					  @RequestParam(value="test", required=false, defaultValue="spring") String test){
//		System.out.println("id2 : " + userId);
//		System.out.println("pwd2 : " + userPwd);
//		System.out.println("test : " + test);
//	}
	
	//3. @RequestParam 생략
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(String id, String pwd) {
//		System.out.println("id3 : " + id);
//		System.out.println("pwd3 : " + pwd);
//	}
	
	// 4. @ModelAttribute 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m) {
//		System.out.println("id4 : " + m.getId());
//		System.out.println("pwd4 : " + m.getPwd());
//	}
	
//	// 5. @ModelAttribute 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(Member m, HttpSession session) {
//		// 이전에 사용했던 new MemberService() 객체를 만드는 방식은 주도권이 나에게 있으며 결합도가 높아진다.
//		// 결합도가 높다는 것을 확인할 수 있는 2가지는?
//		// 1. 클래스명 변경에 직접적인 영향을 받음
//		// 2. 매 요청마다 새로운 service 객체를 생성
//
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
//		}else {
//			
//		}
//	}
	
	/************************ 전달하고자하는 데이터가 있을 경우에 대한 방법  ********************************/
	
//	// 1.Model객체 사용
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, HttpSession session, Model model) {
//		// 이전에 사용했던 new MemberService() 객체를 만드는 방식은 주도권이 나에게 있으며 결합도가 높아진다.
//		// 결합도가 높다는 것을 확인할 수 있는 2가지는?
//		// 1. 클래스명 변경에 직접적인 영향을 받음
//		// 2. 매 요청마다 새로운 service 객체를 생성
//
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);	//세션에 담기에 Model이 필요 x
//			//return "../home";	//얘는 포워드 방식
//			return "redirect:home.do";	//리다이렉트가 됨.
//		}else {
//			model.addAttribute("msg","로그인에 실패하였습니다.");	//model이 곧 request에 담는것.
//			return "../common/errorPage";
//		}
//	}
	
//	// 2.ModelAndView 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public ModelAndView login(Member m, HttpSession session, ModelAndView mv) {
//		// 이전에 사용했던 new MemberService() 객체를 만드는 방식은 주도권이 나에게 있으며 결합도가 높아진다.
//		// 결합도가 높다는 것을 확인할 수 있는 2가지는?
//		// 1. 클래스명 변경에 직접적인 영향을 받음
//		// 2. 매 요청마다 새로운 service 객체를 생성
//
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);	
//			//mv.setViewName("../home");
//			mv.setViewName("redirect:home.do");
//		}else {
//			mv.addObject("msg", "로그인에 실패했습니다.");
//			mv.setViewName("../common/errorPage");
//		}
//		
//		return mv;
//	}
	/*
	 * 
	 * 모델 엔드뷰는 반환값이 모델 앤드 뷰로 바꾸어 주어야함*/
	
	// 여기 보자
//	@RequestMapping("logout.me")
//	public String logout(HttpSession session) {
//		session.invalidate();	//세션어트리뷰트 방식일 시 먹지 않음 해당방식은.
//		return "redirect:home.do";
//	}
	
	// 3.세션에 저장할 때 @SessionAttributes 사용
	// model 객체에 attribute가 추가될 때, 자동으로 키 값을 찾아 세션에 등록
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, Model model) {
//		// 이전에 사용했던 new MemberService() 객체를 만드는 방식은 주도권이 나에게 있으며 결합도가 높아진다.
//		// 결합도가 높다는 것을 확인할 수 있는 2가지는?
//		// 1. 클래스명 변경에 직접적인 영향을 받음
//		// 2. 매 요청마다 새로운 service 객체를 생성
//
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			model.addAttribute("loginUser", loginMember);		//맨~~~위 @Controller위에 추가됨
//			
//			return "redirect:home.do";
//		}else {
//			throw new MemberException("로그인에 실패했습니다.");	//언체크드익셉션(런타임익셉션)으로 바꿔놓았기 때문에 오류가 나지 않음 - 처리불필요
//		}
//		
//	}
	
	@RequestMapping("logout.me")
	public String logout(SessionStatus session) {
		session.setComplete();
		
		return "redirect:home.do";
	}
	
	@RequestMapping("enrollView.me")
	public String enrollView() {
		//logger.debug("회원등록페이지");
		return "memberJoin";
	}
	
	
	@RequestMapping("minsert.me")
	public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
														 @RequestParam("address1") String address1,
														 @RequestParam("address2") String address2) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		// bcrypt 암호화방식 : 스프링 시큐리티 모듈에서 제공
		// 		암호화  + 랜덤한 salt값
		System.out.println(bcrypt);
		String encPwd = bcrypt.encode(m.getPwd());
		m.setPwd(encPwd);
		
		int result = mService.insertMember(m);
		if(result > 0) {
			return "redirect:home.do";
		}else {
			throw new MemberException("회원 가입에 실패하였습니다.");
		}
	}
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, Model model) {
		
		Member loginMember = mService.memberLogin(m);
		// 계정 암호화 한 것 붙여넣기 -> 카톡방
		if(bcrypt.matches(m.getPwd(), loginMember.getPwd())) {
			model.addAttribute("loginUser", loginMember);		//맨~~~위 @Controller위에 추가됨
			logger.debug(loginMember.getId());
			return "redirect:home.do";
		}else {
			throw new MemberException("로그인에 실패했습니다.");	//언체크드익셉션(런타임익셉션)으로 바꿔놓았기 때문에 오류가 나지 않음 - 처리불필요
		}
		
	}
	
	@RequestMapping("myinfo.me")
	public String myInfoView() {
		return "mypage";
	}
	
	@RequestMapping("mupdateView.me")
	public String mUpdateForm() {
		return "memberUpdateForm";
	}
	
	
	@RequestMapping(value="mupdate.me", method=RequestMethod.POST)
	public String mUpdate(@ModelAttribute Member m, Model model,  @RequestParam("post") String post,
			 										@RequestParam("address1") String address1,
			 										@RequestParam("address2") String address2,
			 										HttpSession session) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		int result = mService.memberUpdate(m);
		
		if(result > 0) {
			Member loginMember = mService.memberLogin(m);
			model.addAttribute("loginUser", loginMember);
			return "mypage";
		}else {
			throw new MemberException("정보수정에 실패했습니다.");	//언체크드익셉션(런타임익셉션)으로 바꿔놓았기 때문에 오류가 나지 않음 - 처리불필요
		}
	}
	
	@RequestMapping("mpwdUpdateView.me")
	public String mpwdUpdateView() {
		return "memberPwdUpdateForm";
	}
	
	@RequestMapping(value="mPwdUpdate.me", method=RequestMethod.POST)
	public String mPwdUpdate(@RequestParam("pwd") String oldPwd, @RequestParam("newPwd1") String newPwd, Model model) {
		Member m = (Member)model.getAttribute("loginUser");
		Member dbMember = mService.memberLogin(m);
		
		int result = 0;
		if(bcrypt.matches(oldPwd, dbMember.getPwd())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", m.getId());
			map.put("newPwd", bcrypt.encode(newPwd));
			
			result = mService.updatePassword(map);
		}
		
		if(result <= 0) {
			throw new MemberException("비밀번호 수정에 실패했습니다.");
		}
		
		return "redirect:home.do";
		
	}
	
	@RequestMapping("mdelete.me")
	public String mDelete(@RequestParam("id") String id, Model model) {
		int result = mService.mDelete(id);
		if(result <= 0) {
			throw new MemberException("회원탈퇴에 실패했습니다.");
		}
		
		return "redirect:home.do";
	}
	
	
	@RequestMapping(value="dupId.me", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String dupId(@RequestParam("id") String id, Model model) {
		int result = mService.duplicateId(id);

		JSONObject job = new JSONObject();
		job.put("result", result);

		return job.toJSONString();
	}
}
