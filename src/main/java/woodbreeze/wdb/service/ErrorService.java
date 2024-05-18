package woodbreeze.wdb.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Error;
import woodbreeze.wdb.domain.ErrorType;
import woodbreeze.wdb.domain.Inspection;
import woodbreeze.wdb.repository.ErrorRepository;
import woodbreeze.wdb.repository.InspectionRepository;

import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ErrorService {

    private final ErrorRepository errorRepository;
    private final InspectionRepository inspectionRepository;

    // 에러 저장
    @Transactional
    public void saveError(String errorCode, String errorMessage, String errorName, String errorType) {
        Error error = new Error();
        error.setErrorCode(errorCode);
        error.setErrorMessage(errorMessage);
        error.setErrorName(errorName);
        error.setErrorType(ErrorType.valueOf(errorType));
        errorRepository.saveError(error);
    }


    // 모든 에러 조회
    public List<Error> findAllErrors() {
        return errorRepository.findAllErrors();
    }

    // 랜덤으로 에러 발생
    @Transactional
    public String getRandomErrorMessage() {
        List<Error> allErrors = errorRepository.findAllErrors();
        if (!allErrors.isEmpty()) {
            int randomIndex = new Random().nextInt(allErrors.size());
            Error randomError = allErrors.get(randomIndex);
            return randomError.getErrorMessage();
        } else {
            return "No errors found";
        }
    }


    @Transactional
   public Error randomError() {
        List<Error> allErrors = errorRepository.findAllErrors();
        if (!allErrors.isEmpty()) {
            int randomIndex = new Random().nextInt(allErrors.size());
            return allErrors.get(randomIndex);
        } else {
            return null;
        }
     }

    // 특정 에러 종류 조회
//    public List<Error> findErrorsByType(String errorType) {
//        return errorRepository.findErrorsByType(errorType);
//    }

    // 초기 데이터 삽입
    @PostConstruct
    @Transactional
    public void init() {
        saveError("E001", "재고 부족", "INVENTORY SHORTAGE", "COMMON");
        saveError("E002", "인력 부족", "LABOR SHORTAGE", "COMMON");
        saveError("E003", "기계 고장", "MACHINE FAILURE", "COMMON");
        saveError("E004", "품질 문제", "QUALITY ISSUES", "COMMON");
        saveError("E005", "공정 오류", "PROCESS ERRORS", "COMMON");
        saveError("E006", "환경요인", "ENVIRONMENTAL FACTORS", "COMMON");

        saveError("M001", "잘못된 원재료 구매", "INCORRECT RAW MATERIAL PURCHASE", "PROCESS1");
        saveError("M002", "공급업체 재고 부족", "SUPPLIER INVENTORY SHORTAGE", "PROCESS1");
        saveError("M003", "부적절한 원재료 품질", "INAPPROPRIATE RAW MATERIAL QUALITY", "PROCESS1");
        saveError("M004", "치수 불량", " DEFECT IF THERE IS A DIMENSIONAL ERROR OF ±2CM OR MORE", "PROCESS1");
        saveError("M005", "결함 물질 함유", "DEFECT IF HARMFUL SUBSTANCES EXCEEDING 0.1G PER 1CM^2 AREA ARE DETECTED", "PROCESS1");

        saveError("D001", "부적절한 제품 디자인", "INAPPROPRIATE PRODUCT DESIGN", "PROCESS2");
        saveError("D002", "잘못된 제품 기획", "INCORRECT PRODUCT PLANNING", "PROCESS2");
        saveError("D003", "제품 디자인의 안전 문제", "SAFETY ISSUES IN PRODUCT DESIGN", "PROCESS2");
        saveError("D004", "디자인 부적합", "DEFECT IF THERE ARE SAFETY OR FUNCTIONAL ISSUES IN THE PRODUCT DESIGN", "PROCESS2");

        saveError("P001", "가공 과정 중 발생한 실수", "MISTAKES IN PROCESSING STAGE", "PROCESS3");
        saveError("P002", "기계 고장", "MACHINE FAILURE", "PROCESS3");
        saveError("P003", "가공 과정에서의 잘못된 치수", "INCORRECT DIMENSIONS IN PROCESSING", "PROCESS3");
        saveError("P004", "가공 중 발생한 환경 오염", "ENVIRONMENTAL CONTAMINATION DURING PROCESSING", "PROCESS3");
        saveError("P005", "치수 오차", "DEFECT IF THERE IS A DIMENSIONAL ERROR OF ±1CM OR MORE", "PROCESS3");
        saveError("P006", "표면 결함", "DEFECT IF THERE ARE FLAWS LIKE DENTS, TEARS, OR BREAKAGE ON THE SURFACE", "PROCESS3");

        saveError("A001", "부품 불일치로 인한 조립 오류", "PARTS MISMATCH ASSEMBLY ERROR", "PROCESS4");
        saveError("A002", "조립 과정에서의 부품 손상 또는 분실", "PART DAMAGE OR LOSS DURING ASSEMBLY", "PROCESS4");
        saveError("A003", "조립 라인의 인력 부족으로 인한 생산 지연", "PRODUCTION DELAY DUE TO LABOR SHORTAGE IN ASSEMBLY LINE", "PROCESS4");
        saveError("A004", "부품 불일치", "DEFECT IF ASSEMBLED PRODUCT PARTS DO NOT MATCH SPECIFIED DIMENSIONS OR IF COMPONENTS ARE MISSING", "PROCESS4");

        saveError("S001", "표면 처리 과정에서의 페인트 문제", "PAINT PROBLEMS IN SURFACE TREATMENT PROCESS", "PROCESS5");
        saveError("S002", "마감 작업 중 발생하는 오염 또는 흠집", "CONTAMINATION OR SCRATCHES DURING FINISHING WORK", "PROCESS5");
        saveError("S003", "마감 작업 중 발생한 잘못된 색상", "INCORRECT COLOR DURING FINISHING WORK", "PROCESS5");
        saveError("S004", "표면 처리 과정에서의 화학 물질 누출", "CHEMICAL LEAKAGE IN SURFACE TREATMENT PROCESS", "PROCESS5");
        saveError("S005", "부적절한 마감", "DEFECT IF IMPROPER PAINT USAGE OR UNEVEN FINISHING ON THE SURFACE CAUSES ISSUES", "PROCESS5");

        saveError("Q001", "검사 기준 부적합", "UNSUITABLE INSPECTION CRITERIA", "PROCESS6");
        saveError("Q002", "품질 검사 장비 불량", "DEFECTIVE QUALITY INSPECTION EQUIPMENT", "PROCESS6");
        saveError("Q003", "치수 및 표면 검사", "DEFECT IF THERE ARE DIMENSIONAL ERRORS OR FLAWS EXCEEDING ±0.5CM", "PROCESS6");

        saveError("F001", "잘못된 포장으로 인한 제품 손상", "PRODUCT DAMAGE DUE TO INCORRECT PACKAGING", "PROCESS7");
        saveError("F002", "물류 문제로 인한 납품 지연", "DELIVERY DELAY DUE TO LOGISTICS ISSUES", "PROCESS7");
        saveError("F003", "포장 결함", "DEFECT IF THERE ARE DENTS OR DAMAGE ON THE SURFACE OR IF SUFFICIENT PROTECTION IS NOT PROVIDED DURING PACKAGING", "PROCESS7");
    }

}


