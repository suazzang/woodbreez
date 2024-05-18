package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.domain.Error;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.exception.NotEnoughStockException;
import woodbreeze.wdb.repository.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import static org.hibernate.query.sqm.tree.SqmNode.log;
import static woodbreeze.wdb.domain.InspectionStatus.PASS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProcessService {

    private final ProcessRepository processRepository;
    private final LotRepository lotRepository;
    private final InspectionService inspectionService;
    private final ControlStatusService controlStatusService;
    private final ControlStatusRepository controlStatusRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final ErrorService errorService;


    @Transactional
    public void process1(Orders orders) {
        // 주문 번호를 사용하여 정보 조회
        Orders retrievedOrder = orderService.findByWorkOrderId(orders.getWorkOrderId());
        // 주문 정보의 제품명과 계획 수량 확인
        ProductName productName = retrievedOrder.getProductName();
        int planQTY = retrievedOrder.getPlanQTY();
        log.info("주문 정보 - 제품명: {}, 계획 수량: {}", productName, planQTY);

        // ControlStatus 객체 생성
        ControlStatus controlStatus = controlStatusService.findOne(1L);
        // ControlStatus의 planQTY 설정
        controlStatus.setPlanQTY(retrievedOrder.getPlanQTY());
        controlStatusService.saveControlStatus(controlStatus);
        log.info("제품 수량이 저장되었습니다.");

        // 해당 제품명에 매핑된 원자재 가져오기
        MaterialName materialName = productService.getMaterialNameForProduct(productName);
        // 재고 차감
        productService.restockMaterial(materialName, orders); //  해당 메서드에서 차감, 수량 저장
        
        Process process1 = processRepository.findById(1L);
        // Process 객체와 Product 객체를 인자로 전달하여 공정 실행
        executeProcess(process1, orders, controlStatus);
        log.info("MATERIAL 생산정보 - 계획수량: {}, 반제품: {}, 불량품: {}", controlStatus.getPlanQTY(), controlStatus.getDefects(),controlStatus.getDefective());
        // 다음 공정 실행
        process2(orders, controlStatus, retrievedOrder);
    }

    @Transactional
    public void process2(Orders orders, ControlStatus controlStatus,Orders retrievedOrder) {
        log.info("공정 2를 실행합니다.");
        Process process2 = processRepository.findById(2L);
        ControlStatus controlStatus2 = controlStatusService.findOne(2L);
        controlStatus2.setPlanQTY(retrievedOrder.getPlanQTY());
        controlStatusService.saveControlStatus(controlStatus2);
        executeProcess(process2, orders, controlStatus2);
        log.info("ASSEMBLY 생산정보 - 계획수량: {}, 반제품: {}, 불량품: {}", controlStatus2.getPlanQTY(), controlStatus2.getDefects(),controlStatus2.getDefective());
        process3(orders, controlStatus, retrievedOrder);
    }

    @Transactional
    public void process3(Orders orders, ControlStatus controlStatus, Orders retrievedOrder) {
        log.info("공정 3을 실행합니다.");
        Process process3 = processRepository.findById(3L);
        ControlStatus controlStatus3 = controlStatusService.findOne(3L);
        controlStatus3.setPlanQTY(retrievedOrder.getPlanQTY());
        controlStatusService.saveControlStatus(controlStatus3);
        executeProcess(process3, orders, controlStatus3);
        log.info("SURFACE 생산정보 - 계획수량: {}, 반제품: {}, 불량품: {}", controlStatus3.getPlanQTY(), controlStatus3.getDefects(),controlStatus3.getDefective());
        process4(orders, controlStatus, retrievedOrder);
    }

    @Transactional
    public void process4(Orders orders, ControlStatus controlStatus, Orders retrievedOrder) {
        log.info("공정 4를 실행합니다.");
        Process process4 = processRepository.findById(4L);
        ControlStatus controlStatus4 = controlStatusService.findOne(4L);
        controlStatus4.setPlanQTY(retrievedOrder.getPlanQTY());
        controlStatusService.saveControlStatus(controlStatus4);
        // 재고 차감 (페인트)
        productService.restockMaterial(MaterialName.ORGANICPAINT, orders); //  해당 메서드에서 차감, 수량 저장
        executeProcess(process4, orders, controlStatus4);
        log.info("PAINT 생산정보 - 계획수량: {}, 반제품: {}, 불량품: {}", controlStatus4.getPlanQTY(), controlStatus4.getDefects(),controlStatus4.getDefective());
        process5(orders, controlStatus, retrievedOrder);
    }

    @Transactional
    public void process5(Orders orders, ControlStatus controlStatus, Orders retrievedOrder) {
        log.info("공정 5을 실행합니다.");
        Process process5 = processRepository.findById(5L);
        ControlStatus controlStatus5 = controlStatusService.findOne(5L);
        controlStatus5.setPlanQTY(retrievedOrder.getPlanQTY());
        controlStatusService.saveControlStatus(controlStatus5);
        executeProcess(process5, orders, controlStatus5);
        log.info("QUALITY 생산정보 - 계획수량: {}, 반제품: {}, 불량품: {}", controlStatus5.getPlanQTY(), controlStatus5.getDefects(),controlStatus5.getDefective());
        process6(orders, controlStatus, retrievedOrder);
    }

    @Transactional
    public void process6(Orders orders, ControlStatus controlStatus, Orders retrievedOrder) {
        log.info("공정 6을 실행합니다.");
        Process process6 = processRepository.findById(6L);
        ControlStatus controlStatus6 = controlStatusService.findOne(6L);
        controlStatus6.setPlanQTY(retrievedOrder.getPlanQTY());
        // 여기서의 반제품은 완제품이 되어야 함.
        executeProcess(process6, orders, controlStatus6);
        controlStatus6.setFault(controlStatus6.getDefects());
        log.info("PACKAGING 생산정보 - 계획수량: {}, 완제품: {}, 불량품: {}", controlStatus6.getPlanQTY(), controlStatus6.getFault(),controlStatus6.getDefective());
        // 주문 상태를 COMPLETED로 설정
        orders.setOrderStatus(OrderStatus.COMPLETED);
        orderService.saveOrder(orders);
        controlStatusService.saveControlStatus(controlStatus6);
        log.info("주문 상태가 변경되었습니다.");
    }

    
    //전체 공정
    @Transactional
    public void executeProcess(Process process, Orders orders, ControlStatus controlStatus) {
        log.info("공정을 시작합니다.");
        boolean isRunning = controlStatusRepository.isRunning();
        if (!isRunning) {
            log.info("공정을 건너뜁니다.");
            return;
        }
        // 목표 개수에 도달할 때까지 반복하여 공정을 실행합니다.
        while (true) {
            if (controlStatus.getDefects() == controlStatus.getPlanQTY()) {
                log.info("목표 개수를 달성하여 공정을 종료합니다.");
                break;
            }
            // 품질검사 실행
            Inspection inspection = inspectionService.productInspection();
            if (InspectionStatus.FAIL.equals(inspection.getResult())) {
                log.info("불량으로 판정되었습니다.");
                String randomErrorMessage = errorService.getRandomErrorMessage(); // 랜덤 에러 가져오기
                Error randomError = errorService.randomError();
                inspection.setError(randomError); // 에러 저장
                inspection.setErrorMessage(randomErrorMessage); // 에러 이름 저장
                controlStatus.setDefective(controlStatus.getDefective() + 1); // 불량품 개수 업데이트
                inspection.setProcessId(controlStatus.getId());
            } else {
                log.info("양품으로 판정되었습니다.");
                controlStatus.setDefects(controlStatus.getDefects() + 1); // 반제품 개수 업데이트
                inspection.setProcessId(controlStatus.getId());
            }
            // controlStatus를 데이터베이스에 저장
            controlStatusRepository.save(controlStatus);
        }
    }
    
    public ProcessName getProcessNameById(Long processId) {
        // 프로세스 아이디로 프로세스를 조회하여 해당 프로세스의 이름을 반환
        Process process = processRepository.findById(processId);
        if (process != null) {
            return process.getProcessName();
        } else {
            // 해당 아이디로 프로세스를 찾을 수 없는 경우 예외 처리
            throw new IllegalArgumentException("프로세스를 찾을 수 없습니다. 프로세스 아이디: " + processId);
        }
    }

    //하나찾기
    public Process findOne(Long processId) {
        return processRepository.findById(processId);
    }

    //모두 찾기
    public List<Process> findProcess() {
        return processRepository.findAll();
    }



    // 저장
    public void lotSave(Product product) {
        // Lot 업데이트
        lotRepository.updateLot(product.getLot());
    }

    // 로트 넘버 추가 메서드 (프로젝트에서 받아옴)
    public void updateLotNumber(Product product, String newLotNumber) {
        product.getLot().setLotnumber(newLotNumber);
        lotRepository.updateLot(product.getLot());
    }

}
