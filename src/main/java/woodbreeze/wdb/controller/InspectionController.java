package woodbreeze.wdb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woodbreeze.wdb.domain.Inspection;
import woodbreeze.wdb.domain.InspectionStatus;
import woodbreeze.wdb.service.InspectionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;

    // 검수 진행
    @PostMapping("/perform-inspection")
    public ResponseEntity<Inspection> performInspection() {
        Inspection inspection = inspectionService.productInspection();
        return new ResponseEntity<>(inspection, HttpStatus.CREATED);
    }

    // 전체 검수 조회
    @GetMapping("/all-inspections")
    public ResponseEntity<List<Inspection>> getAllInspections() {
        List<Inspection> inspections = inspectionService.findAllInspections();
        return new ResponseEntity<>(inspections, HttpStatus.OK);
    }

    // 특정 공정의 검수 조회
    @GetMapping("/inspections-by-process/{processId}")
    public ResponseEntity<List<Inspection>> getInspectionsByProcess(@PathVariable Long processId) {
        List<Inspection> inspections = inspectionService.findInspectionsByProcessId(processId);
        return new ResponseEntity<>(inspections, HttpStatus.OK);
    }
}
