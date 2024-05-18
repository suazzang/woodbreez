package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Inspection;
import woodbreeze.wdb.domain.InspectionStatus;
import woodbreeze.wdb.repository.InspectionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    @Transactional
    public Inspection productInspection() {
        log.info("검수를 진행 중입니다.");

        // 불량률을 50%로 설정
        boolean isDefective = Math.random() < 0.5;

        Inspection inspection = new Inspection();

        // 불량인 경우
        if (isDefective) {
            inspection.setResult(InspectionStatus.FAIL);
        } else {
            // 정상인 경우
            inspection.setResult(InspectionStatus.PASS);
        }

        // 결과를 저장
        inspectionRepository.save(inspection);
        log.info("검수가 완료되었습니다.");
        return inspection;
    }

    // 전체 검수 조회
    public List<Inspection> findAllInspections() {
        return inspectionRepository.findAllInspections();
    }

    // 특정 공정의 검수 조회
    public List<Inspection> findInspectionsByProcessId(Long processId) {
        return inspectionRepository.findInspectionsByProcessId(processId);
    }
}
