package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.exception.NotEnoughStockException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Slf4j
//@Transactional(readOnly = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MaterialName materialName; //원재료이름
    @Enumerated(EnumType.STRING)
    private ProductName productName; //제품이름
    private LocalDate dateReceived; // 예상시간
    private String manufacturer; // 제조회사
    private int materialQuantity; // 원자재 수
    private LocalDate expiry; // 유통기한

    @ManyToOne
    @JoinColumn(name = "process_id") //
    private Process process;

    @ManyToOne
    @JoinColumn(name = "lotnumber")
    private Lot lot;

    @OneToMany(mappedBy = "product")
    private List<Orders> orders;

    // 원자재 전체 추가 메서드
    public void allMaterial(Long id, MaterialName materialName, Integer quantity, LocalDate dateReceived, String manufacturer, LocalDate expiry) {
        this.id = id;
        this.materialName = materialName;
        this.materialQuantity = quantity;
        this.dateReceived = dateReceived;
        this.manufacturer = manufacturer;
        this.expiry = expiry;
    }

    // Product 클래스에 Lot 번호를 업데이트하는 메서드
    public void updateLotNumber(String newLotNumber) {
        this.lot.updateLotNumber(newLotNumber);
    }

    // 원자재 재고 증가, Lot 번호와 함께 저장
    public void addStock(MaterialName materialName, int quantity, Lot lot) {
        allMaterial(null, materialName, quantity, LocalDate.now(), "Manufacturer", expiry);
        this.lot = lot;
    }

}
