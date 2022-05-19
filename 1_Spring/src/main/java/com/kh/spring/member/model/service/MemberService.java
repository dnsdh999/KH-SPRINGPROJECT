package com.kh.spring.member.model.service;

import java.util.HashMap;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {

	Member memberLogin(Member m);

	int insertMember(Member m);

	int memberUpdate(Member m);

	int mPwdUpdate(Member m);

	int updatePassword(HashMap<String, String> map);

	int mDelete(String id);

	int duplicateId(String id);

}
