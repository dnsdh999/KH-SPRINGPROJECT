package com.kh.spring.member.model.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository("mDAO")
public class MemberDAO {

	public Member memberLogin(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectOne("memberMapper.memberLogin", m);
	}

	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.insert("memberMapper.insertMember", m);
	}

	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		System.out.println(m);
		return sqlSession.update("memberMapper.updateMember", m);
	}

	public int mPwdUpdate(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.update("memberMapper.mPwdUpdate", m);
	}

	public int updatePassword(SqlSessionTemplate sqlSession, HashMap<String, String> map) {
		return sqlSession.update("memberMapper.updatePassword", map);
	}

	public int mDelete(SqlSessionTemplate sqlSession, String id) {
		return sqlSession.update("memberMapper.mDelete", id);
	}

	public int duplicateId(SqlSessionTemplate sqlSession, String id) {
		return sqlSession.selectOne("memberMapper.duplicateId", id);
	}

}
