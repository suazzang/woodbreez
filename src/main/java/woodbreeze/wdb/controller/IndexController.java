package woodbreeze.wdb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import woodbreeze.wdb.domain.Grade;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String indexLogin(HttpServletRequest request, Model model){
        //세션에 회원 데이터 x index
        HttpSession session = request.getSession(false);
        if (session == null){
            return "index";
        }

        //로그인
        //로그인 시점에 세션에 보관한 회원객체 찾기
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션 회원데이터 x index
        if (loginMember == null){
            return "index";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);

        if (loginMember.getGrade() == Grade.ADMIN){
            return "adminMember";
        }else {
            return "generalMember";
        }
    }
}
