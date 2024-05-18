package woodbreeze.wdb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.service.ControlStatusService;
import woodbreeze.wdb.service.LotService;
import woodbreeze.wdb.service.MemberService;
import woodbreeze.wdb.service.ProductService;
import woodbreeze.wdb.domain.ControlStatus;

import java.lang.Error;
import java.time.LocalDate;

@Slf4j
@Component
public class DummyDataLoader implements CommandLineRunner {

    private final ProductService productService;
    private final MemberService memberService;
    private final ControlStatusService controlStatusService;

    @Autowired
    public DummyDataLoader(ProductService productService, MemberService memberService, ControlStatusService controlStatusService) {
        this.productService = productService;
        this.memberService = memberService;
        this.controlStatusService = controlStatusService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("더미 데이터를 생성합니다.");

        // 더미 사용자 생성
        log.info("더미 사용자를 생성합니다.");
        Member member = new Member();
        member.setLoginId("test");
        member.setName("테스트");
        member.setPassword("1234");
        member.setGrade(Grade.ADMIN);

        // 이미 저장된 상태인지 확인 후 데이터 저장
        if (memberService.findLoginId(member.getLoginId()) == null) {
            memberService.save(member);
            log.info("더미 사용자가 생성되었습니다.");
        } else {
            log.info("더미 사용자가 이미 존재합니다.");
        }

        // 로그인 상태를 가정하여 더미 제품 데이터 생성
//        log.info("더미 제품 데이터를 생성합니다.");
//        Product product1 = new Product();
//        product1.setMaterialName(MaterialName.MAPLE);
//        product1.setMaterialQuantity(100);
//        product1.setManufacturer(String.valueOf(CompanyName.대림상사));
//        productService.saveProduct(product1);
//
//        Product product2 = new Product();
//        product2.setMaterialName(MaterialName.ORGANICPAINT);
//        product2.setMaterialQuantity(100);
//        product2.setManufacturer(String.valueOf(CompanyName.대림상사));
//        productService.saveProduct(product2);
//
//
//        product1.setDateReceived(LocalDate.now().minusDays(30));
//        product1.setExpiry(LocalDate.now().plusDays(365));
//
//        product2.setDateReceived(LocalDate.now().minusDays(30));
//        product2.setExpiry(LocalDate.now().plusDays(365));
//
//        log.info("더미 제품 데이터가 생성되었습니다.");

        // 더미 공정 상태 데이터 생성
        log.info("더미 공정 상태 데이터를 생성합니다.");
        ControlStatus controlStatus1 = new ControlStatus();
        controlStatus1.setId(1L);
        controlStatus1.setRunning(true);
        controlStatus1.setProcess_name("MATERIAL");

        ControlStatus controlStatus2 = new ControlStatus();
        controlStatus2.setId(2L);
        controlStatus2.setRunning(true);
        controlStatus2.setProcess_name("ASSEMBLY");

        ControlStatus controlStatus3 = new ControlStatus();
        controlStatus3.setId(3L);
        controlStatus3.setRunning(true);
        controlStatus3.setProcess_name("SURFACE");

        ControlStatus controlStatus4= new ControlStatus();
        controlStatus4.setId(4L);
        controlStatus4.setRunning(true);
        controlStatus4.setProcess_name("PAINT");

        ControlStatus controlStatus5 = new ControlStatus();
        controlStatus5.setId(5L);
        controlStatus5.setRunning(true);
        controlStatus5.setProcess_name("QUALITY");

        ControlStatus controlStatus6 = new ControlStatus();
        controlStatus6.setId(6L);
        controlStatus6.setRunning(true);
        controlStatus6.setProcess_name("PACKAGING");

        controlStatusService.save(controlStatus1);
        controlStatusService.save(controlStatus2);
        controlStatusService.save(controlStatus3);
        controlStatusService.save(controlStatus4);
        controlStatusService.save(controlStatus5);
        controlStatusService.save(controlStatus6);



        log.info("더미 공정 상태 데이터가 생성되었습니다.");

        log.info("더미 데이터 생성이 완료되었습니다.");

    }

    // 더미 공정 상태 데이터를 생성하는 메서드
    private void createDummyControlStatus(String processName) {
        ControlStatus controlStatus = new ControlStatus();
        controlStatus.setRunning(false);
        controlStatus.setProcess_name(processName);
        controlStatusService.save(controlStatus);
    }
}