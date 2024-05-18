package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final MemberRepository memberRepository; //memberRepository 불러오기

    public Member login(String loginId,String password){
        Optional<Member> findMember = memberRepository.findLoginId(loginId);
        Member member = findMember.orElse(null); //Optinal이 비어있으면 null값으로 반환
        if (member != null && member.getPassword().equals(password)){
            return member;
        }
        return null;
        //loginId,password 매개변수로 받아서 해당 로그인 아이디 비번 조회. 해당 로그인 아이디가 존재여부를 모르기 때문에 받아올 때 Optional 형태로 반환하여 가져온다. 만약 존재하면 해당 회원을 존재하지 않으면 비어있는 Optional를 반환하기 때문에 비어있다면 null값으로 반환하고 member이 null인지 확인하고 비밀번호를 확인한다.
    }
}
