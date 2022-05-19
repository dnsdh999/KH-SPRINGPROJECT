package com.kh.ajax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.ajax.model.vo.Sample;
import com.kh.ajax.model.vo.User;

@Controller
public class TestController {
	
	@Autowired
	private Sample sam;
	
	@RequestMapping("test.do")
	public void test() {
		System.out.println(sam);
	}
	
	//1.ServletOutputStream을 이용해서 전송
	@RequestMapping("test1.do")
	public void test1(@RequestParam("name") String name, @RequestParam("age") int age, HttpServletResponse response) {	//AJAX도 마찬가지
		System.out.println(name);
		System.out.println(age);
		
		try {
			PrintWriter out = response.getWriter();
			
			if(name.equals("강건강") && age == 20) {
				out.println("ok");
			}else {
				out.println("fail");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//인코딩 방식 1번
//	@RequestMapping("test2.do")
//	@ResponseBody	//이걸 추가해야만이 String으로 반환하지만 페이지 값이 아니라는 것을 나타낼 수 있다. (ajax, JSON사용을 위함)
//	public String test2() {
//		JSONObject job = new JSONObject();
//		job.put("no", 123);
//		job.put("title", "return json object test");
//		try {
//			job.put("writer", URLEncoder.encode("남나눔","UTF-8"));
//			job.put("content", URLEncoder.encode("JSON객체를 뷰로 리턴합니다.","UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		//맵은 순서를 유지하지 않기 때문에 순서가 다르게 나올 수 있다.
//		return job.toJSONString();
//	}
	
	//인코딩 방식 2번
//	@RequestMapping("test2.do")
//	@ResponseBody	//이걸 추가해야만이 String으로 반환하지만 페이지 값이 아니라는 것을 나타낼 수 있다. (ajax, JSON사용을 위함)
//	public void test2(HttpServletResponse response) {
//		JSONObject job = new JSONObject();
//		job.put("no", 123);
//		job.put("title", "return json object test");			
//		job.put("writer", "남나눔");
//		job.put("content", "JSON객체를 뷰로 리턴합니다.");
//		
//		PrintWriter out;
//		response.setContentType("application/json; charset=UTF-8");
//		try {
//			out = response.getWriter();
//			out.println(job);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	//인코딩 방식 3번
	@RequestMapping(value="test2.do", produces="application/json; charset=UTF-8")	//지금 보내는 방식이 어떤 방식인지
	public String test2() {
		JSONObject job = new JSONObject();
		job.put("no", 123);
		job.put("title", "return json object test");
		job.put("writer", "남나눔");
		job.put("content", "JSON객체를 뷰로 리턴합니다.");

		//맵은 순서를 유지하지 않기 때문에 순서가 다르게 나올 수 있다.
		return job.toJSONString();
	}
	
//	@RequestMapping(value="test3.do", produces="application/json; charset=UTF-8")	//지금 보내는 방식이 어떤 방식인지
//	public void test3(HttpServletResponse response) {
//		ArrayList<User> list = new ArrayList<User>();
//		list.add(new User("u111","p111","강건강",20,"k111@kh.or.kr","01011112222"));
//		list.add(new User("u222","p222","남나눔",33,"k222@kh.or.kr","01022223333"));
//		list.add(new User("u333","p333","도대담",17,"k333@kh.or.kr","01033334444"));
//		list.add(new User("u444","p444","류라라",23,"k444@kh.or.kr","01044445555"));
//		list.add(new User("u555","p555","문미미",29,"k555@kh.or.kr","01055556666"));
//		
//		JSONArray jArr = new JSONArray();
//		JSONObject userObj = null;
//		for(User u : list) {
//			userObj = new JSONObject();
//			
//			userObj.put("userId", u.getUserId());
//			userObj.put("userPwd", u.getUserPwd());
//			userObj.put("userName", u.getUserName());
//			userObj.put("age", u.getAge());
//			userObj.put("email", u.getEmail());
//			userObj.put("phone", u.getPhone());
//			
//			jArr.add(userObj);
//		}
//		response.setContentType("application/json; charset=UTF-8");
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.println(jArr);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//		
//	}
	
	
	//String타입으로 반환하는 방식 - test3
	@RequestMapping(value="test3.do", produces="application/json; charset=UTF-8")	//지금 보내는 방식이 어떤 방식인지
	@ResponseBody	//추가 필요!
	public String test3(HttpServletResponse response) {
		ArrayList<User> list = new ArrayList<User>();
		list.add(new User("u111","p111","강건강",20,"k111@kh.or.kr","01011112222"));
		list.add(new User("u222","p222","남나눔",33,"k222@kh.or.kr","01022223333"));
		list.add(new User("u333","p333","도대담",17,"k333@kh.or.kr","01033334444"));
		list.add(new User("u444","p444","류라라",23,"k444@kh.or.kr","01044445555"));
		list.add(new User("u555","p555","문미미",29,"k555@kh.or.kr","01055556666"));
		
		JSONArray jArr = new JSONArray();
		JSONObject userObj = null;
		for(User u : list) {
			userObj = new JSONObject();
			
			userObj.put("userId", u.getUserId());
			userObj.put("userPwd", u.getUserPwd());
			userObj.put("userName", u.getUserName());
			userObj.put("age", u.getAge());
			userObj.put("email", u.getEmail());
			userObj.put("phone", u.getPhone());
			
			jArr.add(userObj);
		}
//		response.setContentType("application/json; charset=UTF-8");
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.println(jArr);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
		System.out.println(jArr);
		return jArr.toJSONString();
		
	}
	
//	@RequestMapping(value="test2.do", produces="application/json; charset=UTF-8")	//지금 보내는 방식이 어떤 방식인지
//	public String test2() {
//		JSONObject job = new JSONObject();
//		job.put("no", 123);
//		job.put("title", "return json object test");
//		job.put("writer", "남나눔");
//		job.put("content", "JSON객체를 뷰로 리턴합니다.");
//
//		//맵은 순서를 유지하지 않기 때문에 순서가 다르게 나올 수 있다.
//		return job.toJSONString();
//	}
}
