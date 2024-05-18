package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
//회원 페이지
@Entity
@Getter @Setter
public class Member {

    @Id //해당 프로퍼티가 테이블의 주키(primary key) 역할
    @GeneratedValue // 주키의 값을 위한 자동 생성 전략을 명시하는데 사용한다.
    @Column(name = "member_id")
    private Long id; //회원 아아디

    private String loginId;//로그인 아이디
    private String password; //회원 비밀번호
    private String name;  // 회원 이름

    @Enumerated(EnumType.STRING)
    private Grade grade; //회원 등급

    @OneToMany(mappedBy = "member")
    private List<Orders> order = new ArrayList<>(); //Order클래스에 member과 맵핑 1이기 때문에 그림자 역할
}
