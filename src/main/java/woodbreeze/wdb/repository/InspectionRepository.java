package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Inspection;
import woodbreeze.wdb.domain.InspectionStatus;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InspectionRepository {

    private final EntityManager em;

    // 저장
    public void save(Inspection inspection) {
        em.persist(inspection);
    }

    // 전체 검수 조회
    public List<Inspection> findAllInspections() {
        return em.createQuery("SELECT i FROM Inspection i", Inspection.class)
                .getResultList();
    }

    // PASS 또는 FAIL인 검수 조회
    public List<Inspection> findInspectionsByStatus(InspectionStatus status) {
        return em.createQuery("SELECT i FROM Inspection i WHERE i.result = :status", Inspection.class)
                .setParameter("status", status)
                .getResultList();
    }

// 특정 공정의 검수 조회
public List<Inspection> findInspectionsByProcessId(Long processId) {
    return em.createQuery("SELECT i FROM Inspection i WHERE i.processId = :processId", Inspection.class)
            .setParameter("processId", processId)
            .getResultList();
}
}
