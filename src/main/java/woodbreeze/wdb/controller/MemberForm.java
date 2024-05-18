package woodbreeze.wdb.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import woodbreeze.wdb.domain.Grade;

@Getter @Setter
public class MemberForm {

    private Long id;

    @NotEmpty(message = "직원 아이디는 필수입니다.")
    private String loginId;

    @NotEmpty(message = "직원 비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "직원 이름은 필수입니다.")
    private String name;

    private Grade grade;
}
