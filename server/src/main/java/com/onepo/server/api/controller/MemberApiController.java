package com.onepo.server.api.controller;

import com.onepo.server.api.dto.ResponseDto;
import com.onepo.server.api.dto.member.*;
import com.onepo.server.domain.member.Member;
import com.onepo.server.domain.member.SessionConst;
import com.onepo.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원등록
     */
    @PostMapping("/member/signup")
    public ResponseEntity<ResponseDto> save(@Validated @RequestBody MemberSignUpRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return null;
        }
        Member member = new Member();

        member.register(request.getName(), request.getUserId(), request.getPassword(), request.getEmail());
        memberService.join(member);

        return ResponseEntity.ok().body(new ResponseDto("회원가입이 완료되었습니다."));
    }

     /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Validated @RequestBody MemberLoginRequest dto, BindingResult bindingResult, HttpServletRequest request) {

        String findUserId = dto.getUserId();
        Member findMember = memberService.findByUserId(findUserId);
        String findPassword = dto.getPassword();

        Member member = memberService.authenticated(findMember, findPassword);

        if (member.equals(null) || bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("아이디 또는 비밀번호를 확인해주세요."));
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return ResponseEntity.ok().body(new ResponseDto("로그인 되었습니다."));
    }

     /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().body(new ResponseDto("로그아웃 되었습니다."));
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/member/{id}")
    public ResponseEntity<ResponseDto> change(@PathVariable("id") Long id,
                                              @Validated @RequestBody MemberReviseRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto("비밀번호를 다시한번 확인해주세요."));
        }
        memberService.updateMember(id, request);
        return ResponseEntity.ok().body(new ResponseDto("회원정보가 수정되었습니다."));
    }

}
