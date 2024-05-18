package woodbreeze.wdb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import woodbreeze.wdb.domain.Grade;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //가입
    @GetMapping("/member/new")
    public String createMember(Model model, HttpServletRequest request){
        model.addAttribute("form",new MemberForm());
        model.addAttribute("grades", Grade.values());
        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "members/createMember";
        }
        return "redirect:/";
    }
    //저장
    @PostMapping("/member/new")
    public String create(@Valid @ModelAttribute("form") MemberForm form, BindingResult result,Model model){

        if (result.hasErrors()){
            result.rejectValue("grade", "error.grade", "등급을 선택해주세요");
            model.addAttribute("grades", Grade.values());
            return "members/createMember";//에러처리
        }

        Member member = new Member();
        member.setId(form.getId());
        member.setLoginId(form.getLoginId());
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        member.setGrade(form.getGrade());

        memberService.join(member);
        return "redirect:/";
    }
//조회
    @GetMapping("/members")
    public String list(Model model, HttpServletRequest request){
        List<Member> members = memberService.findMember(); // 모든 멤버 가져오기
        model.addAttribute("members",members);

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "members/memberList";
        }
        return "redirect:/";
    }


    //수정
    @GetMapping("member/{id}/edit")
    public String updateMemberForm(@PathVariable("id")Long memberId,Model model,HttpServletRequest request){

        Member member = memberService.findOne(memberId); //수정할 땐 하나씩 하니까 하나의 멤버 가져오기

        MemberForm form = new MemberForm(); //새로운 값 입력하기 위함 form

        form.setId(member.getId());
        form.setLoginId(member.getLoginId());
        form.setName(member.getName());
        form.setPassword(member.getPassword());
        form.setGrade(member.getGrade());

        //값 넣기

        model.addAttribute("form",form);
        model.addAttribute("grades",Grade.values());

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return"members/updateMember";
        }
        return "redirect:/";

    }

    //수정저장
    @PostMapping("member/{id}/edit")
    public String updateMember(@Valid @ModelAttribute("form")MemberForm form,BindingResult result,Model model){

        if (result.hasErrors()){
            model.addAttribute("grades", Grade.values()); //에러처리
            return "members/updateMember";
        }

        Member member = new Member();
        member.setId(form.getId());
        member.setLoginId(form.getLoginId());
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        member.setGrade(form.getGrade());

        //수정한 값 저장


        memberService.save(member);

        return "redirect:/members";

    }
}
