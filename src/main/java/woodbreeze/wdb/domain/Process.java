package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Slf4j // 로그

@Getter @Setter
public class Process {

    @Id
    @GeneratedValue
    @Column(name = "process_id")
    private Long id;

    @OneToMany(mappedBy = "process")
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_id")
    private Control control;

    @OneToMany(mappedBy = "process")
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

    @Enumerated(EnumType.STRING)
    private ProcessName processName;

    private boolean started; // 공정 시작 여부를 나타냄
    private boolean stopped; // 공정 중지 여부를 나타냄



    //랜덤
    public static boolean random() {
        Random random = new Random();
        int randomNum = random.nextInt(10) + 1;
        return randomNum <= 9;
    }
}

