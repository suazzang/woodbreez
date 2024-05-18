package woodbreeze.wdb.controller;

import lombok.Getter;
import lombok.Setter;
import woodbreeze.wdb.domain.MaterialName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ProductForm {
    private Long id;
    private Long materialId;
    private MaterialName materialName;
    private int MaterialQuantity;
    private LocalDate dateReceived; // 입고 날짜
    private String manufacturer; // 제조회사
    private LocalDate expiry; //유통기한

    private List<MaterialName> materialNames;
    private List<Integer> materialsQuantities;

}
