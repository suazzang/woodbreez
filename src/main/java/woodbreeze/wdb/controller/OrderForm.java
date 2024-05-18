package woodbreeze.wdb.controller;

import lombok.Getter;
import lombok.Setter;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.domain.Product;
import woodbreeze.wdb.domain.ProductName;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderForm {

    private Long id;

    private String workOrderId;   // 주문 번호는 문자열로 변경

    private String productDefId;   // 제품 ID 추가

    private int planQTY;   // 수량

    private ProductName productName;   // 제품 이름

    private Member member;   // 주문한 회원

    private Long productId;   // 제품 ID


    //private LocalDateTime planStartTime;   // 예상 시작 시간

    //private LocalDateTime planEndTime;   // 예상 종료 시간


}
