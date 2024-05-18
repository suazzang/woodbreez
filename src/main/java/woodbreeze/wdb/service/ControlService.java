package woodbreeze.wdb.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.repository.ControlRepository;
import woodbreeze.wdb.repository.OrderRepository;
import woodbreeze.wdb.repository.ProcessRepository;

import java.util.*;

import static java.awt.SystemColor.control;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j

public class ControlService {

    private final ControlRepository controlRepository;
    private final ProcessRepository processRepository;

    private final EntityManager em;

    //값을 초기화해서 모두 stopped로 만듦
    public void stopAllProcesses(Control control) {
        control.getProcessRunningStatus().replaceAll((processName, status) -> false);
    }
    //값을 초기화해서 모두 start로 만듦
    public void startAllProcesses(Control control) {
        control.getProcessRunningStatus().replaceAll((processName, status) -> true);
    }

    //저장
    @Transactional
    public void addProcess(Control control, ProcessName processName) {
        // 새로운 프로세스 생성 및 영속 상태로 만듭니다.
        Process process = new Process();
        process.setProcessName(processName);
        em.persist(process); // 새로운 프로세스를 영속 상태로 만듭니다.

        // Control 엔티티에 프로세스 추가
        control.addProcess(process);

        // 변경된 Control 엔티티를 저장
        controlRepository.save(control);
    }

    @Transactional
    public void addProcessGroup(Control control, String... groupLines) {
        if (groupLines.length == 0) {
            throw new IllegalArgumentException("그룹 라인이 지정되지 않았습니다.");
        }

        Map<ProcessName, Boolean> processRunningStatus = control.getProcessRunningStatus();

        for (String processName : groupLines) {
            ProcessName name;
            try {
                name = ProcessName.valueOf(processName);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 프로세스 이름: " + processName);
            }
            processRunningStatus.put(name, false);
        }

        // 수정된 Control 엔티티를 저장
        controlRepository.save(control);
    }

    // 조회 메서드 추가
    public Control findControlById(String controlId) {
        return controlRepository.findById(controlId);
    }
    // 실행 여부 업데이트 메서드 개선
    private void updateRunningStatus(Process process, boolean running) {
        Map<ProcessName, Boolean> processRunningStatus = process.getControl().getProcessRunningStatus();
        processRunningStatus.put(process.getProcessName(), running);
        process.getControl().setProcessRunningStatus(processRunningStatus);
    }

    // 프로세스 중지 메서드 개선
    @Transactional
    public void stopProcess(Process process) {
        // 공정을 중지 상태로 변경
        process.setStopped(true);
        // 실행 여부 업데이트
        updateRunningStatus(process, false);
        // 변경된 Process 엔티티를 저장
        processRepository.save(process);
    }

    // 그룹 공정 시작 로직 개선
    @Transactional
    public void addGroup(Control control, List<ProcessName> processNames, String groupname) {
        if (processNames.size() >= 1 && processNames.size() <= 6) {
            // Control 객체 생성
            control.setGroupname(groupname);
            // 프로세스 이름 맵 업데이트
            Map<ProcessName, Boolean> processRunningStatus = control.getProcessRunningStatus();
            for (ProcessName processName : processNames) {
                processRunningStatus.put(processName, false);
            }
            control.setProcessRunningStatus(processRunningStatus);
            // 수정된 Control 엔티티를 저장
            controlRepository.save(control);
        } else {
            throw new IllegalArgumentException("선택한 생산 라인은 1부터 최대 6개까지 지정할 수 있습니다.");
        }
    }


// 이전 단계의 프로세스가 실행 중인지 확인하는 메서드
private boolean isPreviousProcessRunning(Process process, Map<ProcessName, Boolean> processRunningStatus) {
    List<Process> processes = process.getControl().getProcesses();
    int currentIndex = processes.indexOf(process); // 현재 프로세스의 인덱스 가져오기

    // 이전 단계의 모든 프로세스를 확인하고 실행 중인지 확인합니다.
    for (int i = 0; i < currentIndex; i++) {
        Process previousProcess = processes.get(i);
        if (processRunningStatus.get(previousProcess.getProcessName())) {
            return true; // 이전 단계의 프로세스가 실행 중이면 true 반환
        }
    }
    return false; // 실행 중인 이전 단계의 프로세스가 없으면 false 반환
}
//    // Control 엔티티를 저장하는 메서드
//    public void saveControl(Control control) {
//        controlRepository.save(control);
//    }

}


