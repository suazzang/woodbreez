package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.OrderStatus;
import woodbreeze.wdb.domain.Orders;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Orders order) {
        if (order.getId() == null) { // 주문이 새로운 경우
            em.persist(order);
        } else { // 이미 데이터베이스에 있는 경우
            em.merge(order);
        }
    }

    // 단일 주문 조회
    public Orders findOneOrder(Long id) {
        return em.find(Orders.class, id);
    }

    // 모든 주문 조회
    public List<Orders> findAllOrder() { // 이걸로 조회해서 가져옴
        return em.createQuery("select o from Orders o", Orders.class).getResultList();
    }

    // 주문 상태로 조회
    public List<Orders> findCreatOrder() {
        return em.createQuery("SELECT o FROM Orders o WHERE o.orderStatus = :status", Orders.class)
                .setParameter("status", OrderStatus.CREATED)
                .getResultList();
    }

    // 주문 취소
    public void cancel(Long orderId) {
        Orders orderToCancel = em.find(Orders.class, orderId);
        if (orderToCancel != null) {
            em.remove(orderToCancel);
        }
    }


    // 주문번호로 조회
    public Orders findByWorkOrderId(String workOrderId) {
        return em.createQuery("select o from Orders o where o.workOrderId = :workOrderId", Orders.class)
                .setParameter("workOrderId", workOrderId)
                .getSingleResult();
    }

    // 단일 주문 조회 시 제품 정보 함께 가져오기
// 모든 주문 조회 (프로덕트 포함)
    public List<Orders> findAllOrderWithProduct() {
        return em.createQuery("select distinct o from Orders o left join fetch o.product", Orders.class)
                .getResultList();
    }



}
