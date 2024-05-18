package woodbreeze.wdb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.repository.ControlStatusRepository;
import woodbreeze.wdb.service.ControlStatusService;
import woodbreeze.wdb.service.MemberService;
import woodbreeze.wdb.service.OrderService;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ControlStatusService controlStatusService;
    private final ControlStatusRepository controlStatusRepository;


    // 주문 폼
    @GetMapping("/order/create")
    public String createOrderForm(Model model, HttpServletRequest request) {

        OrderForm orderForm = new OrderForm();
        model.addAttribute("form",orderForm);
        //모든 직원 이름 가져오기

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "orders/orderForm";
        }
        return "redirect:/";
    }

    @PostMapping("/order/create")
    public String createOrder(@ModelAttribute OrderForm form,HttpServletRequest request) {
        ControlStatus controlStatus = controlStatusRepository.findOne(1L);
        Orders orders = new Orders();
        orders.setId(form.getId());
        orders.setWorkOrderId(form.getWorkOrderId());
        int planQTY = form.getPlanQTY(); // 주문량 가져오기
//        controlStatus.setPlanQTY(planQTY);
        orders.setPlanQTY(planQTY);
        orders.setProductName(form.getProductName());

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null && loginMember instanceof Member) {
            Member member = (Member) loginMember;
            orders.setMember(member); // 주문자의 이름 설정
        }

        // 주문 상태를 CREATED로 설정
        orders.setOrderStatus(OrderStatus.CREATED);
        orderService.saveOrder(orders);
        controlStatusService.saveControlStatus(controlStatus);



        return "redirect:/";

    }


    // 관리자 주문 목록 조회
    @GetMapping("/orders")
    public String getOrderList(Model model,HttpServletRequest request) {
        List<Orders> ordersList = orderService.findAllOrders();
        model.addAttribute("orders", ordersList);

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "orders/orderList";
        }
        return "redirect:/";
//        }

    }

    @GetMapping("/orders1")
    public String getOrderList1(Model model,HttpServletRequest request) {
        List<Orders> ordersList = orderService.findAllOrders();
        model.addAttribute("orders", ordersList);

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "orders/orderList1";
        }
        return "redirect:/";
//        }

    }


    // 주문 취소
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId); // 주문 취소 서비스 호출
        } catch (Exception e) {
            return "redirect:/orders?error=cancelFailed";
        }
        return "redirect:/orders";
    }

}
