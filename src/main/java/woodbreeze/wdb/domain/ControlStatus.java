package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
@Getter
@Setter
@Transactional
public class ControlStatus {

    // 다대일 관계 설정
    @OneToMany(mappedBy = "controlStatus")
    private List<Orders> orders;

    @Id
    @GeneratedValue
    private Long id;
    private boolean running; // 공정 실행 상태
    private String process_name; // 공정 이름
    private  int defects; // 반제품
    private  int defective;//불량품
    private  int planQTY; // 발주 수량
    private  int Fault; //완제품



}
