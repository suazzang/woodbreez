package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.ControlStatus;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.domain.Orders;
import woodbreeze.wdb.domain.Product;
import woodbreeze.wdb.repository.ControlStatusRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ControlStatusService {

    private final ControlStatusRepository controlStatusRepository;

    // 저장
    public void save(ControlStatus controlStatus) {controlStatusRepository.save(controlStatus);}

    //아이디 조회
    public ControlStatus findOne(Long id) {
        return controlStatusRepository.findOne(id);
    }

    // 주문 저장
    @Transactional
    public Long saveControlStatus(ControlStatus controlStatus) {
        if (controlStatus.getPlanQTY() < 0) {
            throw new IllegalArgumentException("주문량은 음수일 수 없습니다 : " + controlStatus.getPlanQTY());
        }
        controlStatusRepository.update(controlStatus);
        return controlStatus.getId();
    }

    // ID로 모두 조회
    public List<ControlStatus> ControlStatusById() {
        return controlStatusRepository.findAll();
    }

}
