package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Lot;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.domain.Product;

@Repository
@RequiredArgsConstructor
public class LotRepository {

    private final EntityManager em;


    @Transactional
    public void save(Lot lot) {
        // Lot 저장
        if (lot.getId() == null) {
            em.persist(lot);
        } else {
            em.merge(lot);
        }

        // Lot에 속한 Product 저장
        for (Product product : lot.getProducts()) {
            if (product.getId() == null) {
                product.setLot(lot); // Product에 Lot 설정
                em.persist(product); // 새로운 Product 저장
            } else {
                em.merge(product); // 기존의 Product 저장
            }
        }
    }

    public void updateLot(Lot lot) {
       em.merge(lot);
    }

    // 단건 조회
    public Lot findById(Long id) {
        return em.find(Lot.class, id);
    }

//로트 아이디로 조회
    

//    public void saveLotNumber(String newLotNumber) {
//        em.persist(new Lot(newLotNumber));
//    }
}
