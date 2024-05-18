package woodbreeze.wdb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import woodbreeze.wdb.service.ErrorService;

@Controller
@RequiredArgsConstructor
public class ErrorController {

    private final ErrorService errorService;

    @GetMapping("/errors")
    public String getAllErrors(Model model) {
        // 모든 에러를 조회하여 모델에 추가
        model.addAttribute("errors", errorService.findAllErrors()); // 수정된 부분
        return "errorList"; // 에러 목록 페이지로 리턴
    }

    @GetMapping("/simulateRandomError")
    public String randomError() {
        // 랜덤하게 에러 발생 및 저장
        errorService.randomError();
        return "redirect:/errors"; // 에러 목록 페이지로 리다이렉트
    }
}
