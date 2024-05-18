package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workOrderId; // 주문번호

    private int planQTY;

    // ControlStatus와의 다대일 관계 매핑
    @ManyToOne
    @JoinColumn(name = "control_status_id") // 외래 키 이름 지정
    private ControlStatus controlStatus;

    @Enumerated(EnumType.STRING)
    private ProductName productName; // 제품명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //private Product productDefId; // 제품번호

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; // Product와의 관계 설정


    @Column(name = "group_name")
    private String groupName;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 제품 오더 상태값 (처리중, 완료 등)


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id")
    private Control control;

    //    private LocalDateTime planStartTime; // 예상 시작 시간
//    private LocalDateTime planEndTime; // 예상 종료 시간

    public void cancel(EntityManager em) {
        em.remove(this);//주문 삭제
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELLED;
    }

}
