package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor //Lombok에서 의존성 주입이다.(초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성)
public class MemberRepository {

    private final EntityManager em; //영속성 컨텍스트(Persistence Context)를 관리하며 데이터베이스와의 상호작용을 담당하고,객체와 관계형 데이터베이스 간의 매핑을 처리하고 영속성을 보장


    //저장
    public void saveId(Member member){
        if (member.getId() == null){
            em.persist(member);
        }else {
            em.merge(member);
        }
        //코드 설명 : 내가 입력받은 id값이 null(없다)일 경우 데이터베이스에 em.persist를 통해서 저장을 한다. 만약 동일한 값이 있는 경우 em.merge를 통해서 값을 저장하지 않고 반환하기만 한다.
    }
    //단건조회
    public Member findOne(Long id){
        return em.find(Member.class,id);
        //코드 설명 : Member 클래스에서 id인 엔티티를 DB에 검색하고, 검색한 엔티티가 있는 경우 반환한다. 만약 결과가 없을 경우 null로 반환한다.
    }

    //전체 회원 찾기
    public List<Member> findAll(){
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }

    //이름으로 찾기
    public List<Member> findName(String name){
        List<Member> findName = em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name",name).getResultList();
        return findName;
    }
    //아이디
    public Optional<Member> findLoginId(String longId){
        List<Member> all = findAll();
        for (Member e : all){
            if (e.getLoginId().equals(longId)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
        // 모든 멤버를 가져오기 위해 List로 선언해서 가져오고 findAll에 있는 모든 회원을 all에 담는다.for문을 이용해서 모든 회원을 조회하는데 내가 가져온 id(e)가 있는지 확인한다. 일치하는 회원이 있다면 해당 회원을 담겨있는 Optional객체를 반환하고, 없다면 비어있는 Optinal객체를 반환한다.
    }
}
