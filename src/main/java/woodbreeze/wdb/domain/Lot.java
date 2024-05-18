package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Lot {

    private Long id; // id

    @Id
    @JoinColumn(name = "lotnumber")
    private String lotnumber; // lot 번호
    private String workOrder; // 작업 현황
    private LocalDateTime trackInTime; // 예상 시작 시간
    private LocalDateTime trackOutTime; // 예상 종료 시간
    private LocalDateTime expiry; //유통기한

    @OneToMany(mappedBy = "lot")
    private List<Process> processes = new ArrayList<>();

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();


    //Lot 번호를 업데이트하는 메서드
    public void updateLotNumber(String newLotNumber) {
        this.lotnumber = newLotNumber;
    }

    // 연결 관계
    //@OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    //private List<Product> products = new ArrayList<>();

    //@ManyToOne
   // @JoinColumn(name = "process_id")
    //private Process process;


}