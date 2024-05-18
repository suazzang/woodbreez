package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    // 주문 저장
    @Transactional
    public Long saveOrder(Orders order) {
        if (order.getPlanQTY() < 0) {
            throw new IllegalArgumentException("주문량은 음수일 수 없습니다 : " + order.getPlanQTY());
        }
        order.setWorkOrderId(AAWorkOrderId());

        orderRepository.save(order);
        return order.getId();
    }

    // 주문번호 년, 월, 일 + 랜덤 숫자 3글자
    private String AAWorkOrderId() {
        String today = LocalDate.now().toString().replace("-", "");
        String randomNumber = String.format("%03d", new Random().nextInt(1000));
        return today + randomNumber;
    }

    //
    private String getProductCode(ProductName productName) {
        switch (productName) {
            case HEDGEHOG:
                return "HE";
            case BOWLINGSET:
                return "BO";
            case JENGA:
                return "JE";
            default:
                throw new IllegalArgumentException("Invalid product name: " + productName);
        }
    }



    // 모든 주문 조회 (프로덕트 포함)
    public List<Orders> findAllOrderWithProduct() {
        return orderRepository.findAllOrderWithProduct();
    }

    //상태값으로 조회

    // 단일 주문 조회
    public Orders findOneOrder(Long orderId) {
        return orderRepository.findOneOrder(orderId);
    }

    // 모든 주문 조회
    public List<Orders> findAllOrders() {
        return orderRepository.findAllOrder();
    }

    // 주문번호로 조회
    public Orders findByWorkOrderId(String workOrderId) {
        return orderRepository.findByWorkOrderId(workOrderId);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        orderRepository.cancel(orderId);
    }

    // 크리에이트 주문 조회
    public void findCreatOrder() {
        orderRepository.findCreatOrder();
    }

}




    // 주문 저장 및 처리
//            List<Orders> nextOrders = orderRepository.findAllOrder();
//            // 다음 처리할 목록이 있는지 확인하고 처리합니다.
//            if (nextOrders != null && !nextOrders.isEmpty()) {
//                log.info("다음 처리할 목록이 있습니다. 이어서 실행합니다.");
//                processOrders(nextOrders);
//            } else {
//                log.info("다음 처리할 목록이 없습니다. 러닝 중지합니다.");
//                return; // 다음 처리할 목록이 없으므로 러닝 중지
//            }
    //}


//
//        오더 리스트 확인 순차적으로 진행
//         상태값 크리에이트에서 프로세싱으로 변경
//        executeProcess 실행
//         끝나면 상태값 프로세싱에서 컴플리트로 변경
//         저장

