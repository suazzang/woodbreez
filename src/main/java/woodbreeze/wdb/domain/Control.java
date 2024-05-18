package woodbreeze.wdb.domain;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Slf4j
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupname;
    private String instructions;

    @OneToMany(mappedBy = "control", cascade = CascadeType.ALL)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "control")
    private List<Process> processes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InspectionStatus status;



    //공정 그룹에 해당하는 값
    private boolean process1;
    private boolean process2;
    private boolean process3;
    private boolean process4;
    private boolean process5;
    private boolean process6;

    // 공정별 진행 상태를 나타내는 맵
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<ProcessName, Boolean> processRunningStatus = new EnumMap<>(ProcessName.class);


// 공정 저장
    public void addProcess(Process process) {
        this.processes.add(process);
        this.processRunningStatus.put(process.getProcessName(), false); // 새로운 공정 추가 시, 기본적으로는 진행 중이지 않음(false)
        process.setControl(this);
    }




}