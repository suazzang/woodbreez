package woodbreeze.wdb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.repository.ControlRepository;
import woodbreeze.wdb.repository.ProcessRepository;
import woodbreeze.wdb.service.ControlService;
import woodbreeze.wdb.service.ProcessService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ControlController {

    private final ControlService controlService;
    private final ControlRepository controlRepository;
    private final ProcessRepository processRepository;
    private final ProcessService processService;


    @PostMapping("/{controlId}/addProcess")
    public Control addProcess(@PathVariable String controlId, @RequestBody Process process) {
        Control control = controlRepository.findById(controlId); // controlId에 해당하는 Control 객체 조회
        ProcessName processName = processService.getProcessNameById(process.getId()); // 프로세스의 ID를 사용하여 프로세스 이름을 가져옴
        controlService.addProcess(control, processName); // 프로세스의 processName 필드를 사용하여 처리
        return control; // 수정된 Control 객체 반환
    }


    // 기본값 설정
    

    // 공정 선택 폼 페이지로 이동
    @GetMapping("/processcontrol")
    public String processControl(Model model,HttpServletRequest request) {
        // 모델에 그룹 이름 목록을 추가
        List<String> groupNames = controlRepository.findGroupNames();
        model.addAttribute("groupNames", groupNames);
        // Control 객체를 폼에 바인딩하기 위해 빈 Control 객체도 추가
        model.addAttribute("control", new Control());

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            return "control/processControl";
        }
        return "redirect:/";
    }

    // 클라이언트로부터 공정 선택 데이터를 받아서 처리
    @PostMapping("/processcontrol")
    public String saveControl(@ModelAttribute("control") @Valid Control control, BindingResult bindingResult, @RequestParam("group") String group) {
        if (bindingResult.hasErrors()) {
            log.info("유효성 검사를 시작합니다.");
            // 유효성 검사 오류가 발생한 경우 폼 페이지로 다시 이동
            return "control/processControl";
        }
        control.setGroupname(group); // 클라이언트로부터 받은 그룹 값을 설정
        log.info("그룹 값을 설정합니다.,");
        controlRepository.save(control); // 데이터베이스에 저장
        log.info("저장됐습니다..");
        return "redirect:/processcontrol";
    }


    //공정 중지
    @PostMapping("/stopProcess")
    public String stopSelectedProcesses(@RequestBody List<Long> selectedProcessIds) {
        for (Long processId : selectedProcessIds) {
            Process process = processRepository.findById(processId);
            controlService.stopProcess(process); // 프로세스 중지
            log.info("Process {} stopped.", processId);
        }
        log.info("선택한 공정을 중지합니다");
        return "redirect:/processcontrol";
    }

//    //전체 공정 시작
//    @PostMapping("/allstartProcess")
//    public String allProcessStart(Control control) {
//        // 전체 공정 시작 메서드 호출
//        controlService.startAllProcesses(control);
//        return "redirect:/processcontrol";
//    }
//
//    // 전체 공정 중지
//    @PostMapping("/allstopProcess")
//    public String allProcessStop(Control control) {
//        // 전체 공정 중지 메서드 호출
//        controlService.stopAllProcesses(control);
//        return "redirect:/processcontrol";
//    }
}



