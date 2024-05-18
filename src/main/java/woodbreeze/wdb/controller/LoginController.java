package woodbreeze.wdb.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;  //LoginService 가져오기
    
    //로그인(보여주는 페이지)
    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm",new LoginForm());
        return "login/loginForm";
        //localhost8080 주소값,html에 넣을 이름,localhost8080/login 주소일 때 보여줄 html 위치와 이름
    }

    //로그인(가져오는 정보)
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletRequest request){
        //@Valid는 LoginForm객체의 유효성 검사이고 그 결과를 bindingResult에 담음.HttpServletRequest는 로그인 요청 처리.클라에서 전송된 데이터나 세선정보 등을 가져와서 로그인 수행
        if (bindingResult.hasErrors()){
            return "login/loginForm";
            //bindingResult.hasErrors()는 유효성 검사에 오류가 발생했는지 확인함.오류가 발생하면 login/loginForm을 보여줌
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        //loginService에서 로그인할 때 입력한 아이디와 비밀번호를 가져와서 loginMember에 담음

        if (loginMember == null){
            form.setLoginFail("아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
            //loginMember을 가져왔는데 아이디 또는 비밀번호가 null인 경우 LoginForm에 있는 loginFail에 "아이디 또는 비밀번호가 일치하지 않습니다."를 넣어주고 login/loginForm을 보여줌
        }


        HttpSession session = request.getSession();
        //로그인 성공시 세션이 있으면 반환,세션이 없으면 신규세션 생성(세션의 역할 : 클라와 서버상태를 유지시킨다.클라가 서버로 요청을 보낼 때 세션 식별자를 같이 보냄으로서 서버는 해당 세션을 사용해서 클라 상태 추적이 가능하다.또한 사용자가 로그인을 하면 서버에서 사용자에 대한 정보를 세션에 저장하고, 이후 사용자의 요청이 들어오면 세션으로 사용자를 식별하고, 사용자의 장바구니, 로그인 정보,설정 등을 저장하고 유지함)


        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //세션에 로그인 회원 정보 저장

        return "redirect:/";
    }

    //로그아웃(가져오는 정보)
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션삭제
        HttpSession session = request.getSession(false);
        //true는 세션이 없으면 만들어 버린다.일단 가지고 오는데 없으면 null

        if (session != null){
            session.invalidate();  //세션을 제거한다.
        }

        return "redirect:/";
        //사용자가 로그아웃을 했을 때 실행됨.HttpSession session = request.getSession(false);는 요청에 대한 세션을 가져옴.값이 true이면 세션이 없을 때 세션을 만들기 때문에 false로 세션이 없으면 null값이 반환된다.만약 세션이 null이 아닌 경우 로그아웃 했을 때 세션을 삭제한다.
    }

    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); //시간을 0으로 만든다.
        response.addCookie(cookie);
    }

}
