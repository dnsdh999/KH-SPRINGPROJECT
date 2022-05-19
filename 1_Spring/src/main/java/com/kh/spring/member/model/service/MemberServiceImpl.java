package com.kh.spring.member.model.service;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;


//@Service	//서비스 어노테이션	//이것을 만듬으로써 객체 생성 시 생성자의 이름이 바뀌는 것을 방지할 수 있음.
@Service("mService")	//이렇게 하면 좀더 정확
//어노테이션으로 인해 객체가 만들어졌으나, mService를 컨트롤러의 mService에 집어넣을게~ 하는 내용이 없기 때문에 위와같이 하면 null이 뜬다. (객체가 만들어졌지만 이게 그것이라고 정의해주지 않았기때문에.)
//오토와이어드를 컨트롤러에 추가한다면 객체 주소가 정상적으로 뜨는 것을 확인가능 + 새로고침을 해도 같은 주소값이 계속해서 뜨는 것을 확인할 수 있음.


public class MemberServiceImpl implements MemberService{
	
	@Autowired //의존성 주입
	private MemberDAO mDAO;	//객체 생성이 된것. DAO를 레포지토리로 이름 설정한 뒤 가져오는 방식.

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public Member memberLogin(Member m) {
		return mDAO.memberLogin(sqlSession, m);
	}

	@Override
	public int insertMember(Member m) {
		return mDAO.insertMember(sqlSession, m);
	}

	@Override
	public int memberUpdate(Member m) {
		return mDAO.updateMember(sqlSession, m);
	}

	@Override
	public int mPwdUpdate(Member m) {
		return mDAO.mPwdUpdate(sqlSession, m);
	}

	@Override
	public int updatePassword(HashMap<String, String> map) {
		return mDAO.updatePassword(sqlSession, map);
	}

	@Override
	public int mDelete(String id) {
		return mDAO.mDelete(sqlSession, id);
	}

	@Override
	public int duplicateId(String id) {
		return mDAO.duplicateId(sqlSession, id);
	}

}
