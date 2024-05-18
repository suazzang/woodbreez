package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service //서비스 선언
@Transactional(readOnly = true)//읽기 전용
@RequiredArgsConstructor //Lombok에서 의존성 주입이다.(초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성)
public class MemberService {
    private final MemberRepository memberRepository; //MemberRepository 가져오기

    //회원가입
    @Transactional
    public Long join(Member member){
        //중복회원검증
        overlapMember(member);
        //저장
        memberRepository.saveId(member);
        return member.getId();
        // 코드 설명 : overlapMember을 가서 중복회원인지 확인한다. 중복회원이 아닌경우 memberRepository에 아이디를 저장하고 저장이 되면 ID를 반환한다.
    }

    //저장
    @Transactional
    public Long save(Member member){
        memberRepository.saveId(member);
        return member.getId();
    }

    //중복검증
    private void overlapMember(Member member) {
        Optional<Member> findMember = memberRepository.findLoginId(member.getLoginId());
        if (!findMember.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // 코드 설명 : Optional = 내가 입력받은 값과 같은 회원이 존재할 수도 있고 존재하지 않을 수도 있기 때문에 사용.memberRepository가서 중복회원이 있는지 확인한다. 만약 존재한다면 IllegalStateException 예외를 던져서 "이미 존재하는 회원입니다"라고 알려줍니다.
    }

    //하나 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
        // 코드 설명 : memberRepository에 가서 해당하는 회원을 조회하고 조회한 회원 정보를 반환한다.만약 회원이 없다면 null이 반환된다.
    }

    //회원 전체 조회
    public List<Member> findMember(){
        return memberRepository.findAll();
        // 코드 설명 : memberRepository에 가서 모든 회원을 조회하고 조회한 회원 정보를 반환한다.만약 회원이 없다면 null이 반환된다.
    }

    //로그인 아이디 조회
    public Member findLoginId(String loginId) {
        Optional<Member> optionalMember = memberRepository.findLoginId(loginId);

        if (optionalMember.isPresent()) { // Optional 내에 멤버가 존재하는지 확인
            Member member = optionalMember.get(); // Optional에서 멤버 추출
            // 여기서 비밀번호 비교 등 다른 조건을 적용할 수 있습니다.
            return member;
        } else {
            return null; // Optional 내에 멤버가 존재하지 않는 경우 null 반환
        }
    }

}

