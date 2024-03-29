package com.onepo.server.service;


import com.onepo.server.api.dto.member.PasswordModifyRequest;
import com.onepo.server.domain.member.Member;
import com.onepo.server.exception.NewPasswordWrong;
import com.onepo.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WishService wishService;

    /**
     *
     * @param member
     * @return
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateUserId(member);    //아이디 중복 검사
        memberRepository.save(member);

        return member.getId();
    }

    /**
     *
     * @param member
     * 아이디 중복 검사
     */

    private void validateDuplicateUserId(Member member) {
        List<Member> members = memberRepository.findByUserId(member.getUserId());
        if (!members.isEmpty()) {
            throw new IllegalStateException("동일한 아이디가 이미 존재합니다.");
        }
    }

    /**
     *
     * @param id
     * @return
     * Member 조회 기능
     */
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }


    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByUserId(String userId) {
        List<Member> users = memberRepository.findByUserId(userId);
        return users.get(0);
    }

    public Member findByTokenId(String token) {
        return memberRepository.findOneByUUID(token);
    }

    public Member authenticated(Member member, String password) {
        if (member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }


    /**
     *
     * @param id
     * @param request
     * 비밀번호 변경
     */
    @Transactional
    public void updateMember(String id, PasswordModifyRequest request) {
        Member member = memberRepository.findOneByUUID(id);

        if (request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            member.passwordChange(request.getNewPassword());
        }
        else {
            throw new NewPasswordWrong("새 비밀번호가 일치하지 않습니다.");
        }
    }

}
