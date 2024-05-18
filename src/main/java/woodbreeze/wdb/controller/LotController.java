package woodbreeze.wdb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woodbreeze.wdb.domain.MaterialName;
import woodbreeze.wdb.domain.Product;
import woodbreeze.wdb.service.LotService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor // 클래스에 선언된 모든 final 필드에 대한 생성자를 자동으로 생성해줌
public class LotController {

    private final LotService lotService;

    @PostMapping("/lots")
    public String createLot(@RequestBody Product product) {
        MaterialName materialName = product.getMaterialName();
        LocalDate receivedDate = product.getDateReceived();

        // Lot 생성 서비스 호출
        String lotId = lotService.createLot(materialName.name(), receivedDate);

        // 생성된 Lot ID 반환
        return lotId;
    }
// @RequiredArgsConstructor를 사용하여 생성자가 자동으로 생성되는 경우에는 해당 클래스의 모든 final 필드를 가지고 있는 생성자가 만들어집니다. 그러나 @Autowired 어노테이션을 사용한 생성자는 Spring이 해당 클래스의 빈을 생성할 때 주입할 의존성을 명시적으로 지정하는 역할을 합니다.
//따라서 @Autowired 어노테이션이 있는 생성자가 있으면 Spring은 해당 생성자를 사용하여 의존성을 주입하게 됩니다. 이렇게 함으로써 코드의 가독성을 높이고, Spring이 자동으로 생성한 생성자와 명시적으로 지정한 생성자 간의 충돌을 방지할 수 있습니다.
//결론적으로, @RequiredArgsConstructor가 이미 존재하는 경우에도 @Autowired 어노테이션이 필요한 경우가 있습니다. 특히 Spring의 의존성 주입을 사용해야 하는 경우에 해당합니다.

}
