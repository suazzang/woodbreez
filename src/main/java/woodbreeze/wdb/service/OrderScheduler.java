package woodbreeze.wdb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import woodbreeze.wdb.domain.Orders;
import woodbreeze.wdb.repository.OrderRepository;

import java.util.List;

@Slf4j
@Component
public class OrderScheduler {
    private final OrderService orderService;
    private final ProcessService processService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderScheduler(OrderService orderService, ProcessService processService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.processService = processService;
        this.orderRepository = orderRepository;
    }

    // 매 1분마다 오더 리스트를 체크하는 메서드
    @Scheduled(cron = "0 0/3 0-23 * * *") // 매 1분마다, 매일 오전 8시부터 오후 6시 사이에 작업을 실행
    public void checkOrderList() {
        log.info("OrderList를 확인합니다.");
        List<Orders> ordersList = orderRepository.findCreatOrder(); // 데이터베이스에서 상태가 크리에이트인 오더 리스트를 가져옴
        log.info("OrderList 확인 완료");

        // 각 주문에 대한 처리
        for (Orders order : ordersList) {
            processService.process1(order); // 주문 자체를 인자로 전달하여 공정 실행
        }
    }
}





