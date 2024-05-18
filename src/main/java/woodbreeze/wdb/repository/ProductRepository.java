package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Control;
import woodbreeze.wdb.domain.MaterialName;
import woodbreeze.wdb.domain.Product;
import woodbreeze.wdb.domain.ProductName;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepository {

    private final EntityManager em;

// 추가한 원자재를 저장
public void save(Product product) {
    if (product.getMaterialName() == null) {
        em.persist(product); // 새로운 원자재인 경우
    } else {
        em.merge(product); // 기존의 원자재인 경우
    }
}




    //모두조회
    public List< Product> findAll(){
        return em.createQuery("select m from  Product m",  Product.class).getResultList();
    }

    //아이디로 찾기
    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    // 원자재 이름으로 여러개 찾기
    public List<Product> findByMaterialName(String materialName) {
        return em.createQuery("select p from Product p where p.materialName = :materialName", Product.class)
                .setParameter("materialName", MaterialName.valueOf(materialName))
                .getResultList();
    }


    // 원자재 이름으로 하나 제품 찾기
    public List<Product> findByOneMaterialName(MaterialName materialName) {
        return em.createQuery("select p from Product p where p.materialName = :materialName", Product.class)
                .setParameter("materialName", materialName)
                .getResultList();
    }

    // 제품 이름으로 하나 찾기
    public Product findByProductName(ProductName productName) {
        try {
            return em.createQuery("select p from Product p where p.productName = :productName", Product.class)
                    .setParameter("productName", productName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    //삭제
    public void deleteById(Long id) {
        Product product = em.find(Product.class, id);
        log.info("찾아온 제품 : " +  product);
        if (product != null) {
            em.remove(product);
            log.info("제품 삭제 완료");
        }
    }

    //하나만 삭제
    public void deleteByMaterial(Long id) {
        Product product = em.find(Product.class, id);
        log.info("찾아온 제품 : " +  product);
    }

}

