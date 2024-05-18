package woodbreeze.wdb.service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import woodbreeze.wdb.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.repository.OrderRepository;
import woodbreeze.wdb.repository.ProcessRepository;
import woodbreeze.wdb.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final LotService lotService;




    //저장
    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // 삭제
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    // 원자재 이름으로 제품 조회
    public List<Product> getProductsByMaterialName(String materialName) {
        return productRepository.findByMaterialName(materialName);
    }

    // 모두 찾기
    public List<Product> findProduct() {
        return productRepository.findAll();
    }

    //아이디로 찾기 
    public Product findProductById(Long id) {
        log.info("찾아온 결과 : " + productRepository.findById(id));
        return productRepository.findById(id);
    }


    // 제품명에 원자재 붙여주기
    public MaterialName getMaterialNameForProduct(ProductName productName) {
        Map<ProductName, MaterialName> materialMap = new HashMap<>();
        materialMap.put(ProductName.HEDGEHOG, MaterialName.MAPLE);
        materialMap.put(ProductName.BOWLINGSET, MaterialName.BEECH);
        materialMap.put(ProductName.JENGA, MaterialName.WHITEPINE);

        // 해당 제품명에 붙은 원자재가 없는경우
        MaterialName materialName = materialMap.get(productName);
        if (materialName == null) {
            throw new IllegalArgumentException("해당 제품명에 설정된 원자재가 없습니다.");
        }
        return materialName;
    }

    @Transactional
    public void restockMaterial(MaterialName materialName, Orders orders) {
        // materialName에 해당하는 모든 Product 가져오기
        List<Product> products = productRepository.findByOneMaterialName(materialName);

        for (Product product : products) {
            // 해당 제품의 원자재 수를 감소시킴
            int currentQuantity = product.getMaterialQuantity();
            int newQuantity = currentQuantity - orders.getPlanQTY();
            if (newQuantity < 0) {
                newQuantity = 0;
            }
            product.setMaterialQuantity(newQuantity);
            log.info(materialName.toString() + "의 재고가 감소되었습니다.");
            productRepository.save(product); // 변경된 제품 저장
        }
    }

    private void decreaseMaterialQuantity(Product product) {
        // 현재 원자재 수를 가져옴
        int currentQuantity = product.getMaterialQuantity();

        // 새로운 원자재 수는 현재 원자재 수에서 1을 감소한 값
        int newQuantity = currentQuantity - 1;

        // 최소 원자재 수는 0이어야 함
        if (newQuantity < 0) {
            newQuantity = 0;
        }

        // 새로운 원자재 수 설정
        product.setMaterialQuantity(newQuantity);
        log.info(product.getMaterialName().toString() + "의 재고가 감소되었습니다.");
    }

}