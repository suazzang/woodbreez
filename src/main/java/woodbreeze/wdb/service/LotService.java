package woodbreeze.wdb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Lot;
import woodbreeze.wdb.domain.MaterialName;
import woodbreeze.wdb.domain.Product;
import woodbreeze.wdb.repository.LotRepository;
import woodbreeze.wdb.repository.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final ProductRepository productRepository; // ProductRepository 추가

    // Lot 번호를 생성하고 저장하는 메서드
    @Transactional
    public String createLot(String materialName, LocalDate receivedDate) {
        // Lot ID 생성
        char firstCharacter = materialName.charAt(0);
        String year = String.valueOf(receivedDate.getYear()).substring(2);
        String month = String.valueOf((char) ('A' + receivedDate.getMonthValue() - 1));
        String day = String.format("%02d", receivedDate.getDayOfMonth());
        String lotId = String.valueOf(firstCharacter) + year + month + day;

        // Lot 객체 생성
        Lot lot = new Lot();
        lot.setLotnumber(lotId);
        lot.setTrackInTime(LocalDateTime.now());
        lot.setTrackOutTime(LocalDateTime.now().plusHours(8));
        lot.setWorkOrder("WDB-123");

        // Lot 저장
        lotRepository.save(lot);

        // Product 생성 후 Lot 설정 및 저장
        List<Product> products = productRepository.findByMaterialName(materialName); // ProductRepository 사용
        for (Product product : products) {
            product.setLot(lot); // Product에 Lot 설정
            // Product 저장
            productRepository.save(product); // ProductRepository 사용
        }



        // 생성된 Lot ID 반환
        return lotId;
    }

    // 제품을 로트에서 제거하고 재고를 채우는 메서드
    public void removeProduct(Product product) {
        if (product.getLot() != null) {
            Lot lot = product.getLot();
            lot.getProducts().remove(product); // 제품 제거
            product.setLot(null); // 제품의 로트를 null로 설정하여 로트에서 제거함을 표시
            // 재고 증가
            product.addStock(product.getMaterialName(), product.getMaterialQuantity(), lot); // 로트 정보를 사용하여 재고 증가
        } else {
            System.out.println("제품이 어떤 로트에 속해있지 않습니다.");
        }
    }




}