package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Control;
import woodbreeze.wdb.domain.ProcessName;
import woodbreeze.wdb.domain.ProductName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ControlRepository {
    private final EntityManager em;

    @Transactional
    public Control save(Control control) {
        em.persist(control);
        return control;
    }

    public Control findById(String id) {
        // controlId를 기반으로 Control을 찾도록 수정
        return em.createQuery("SELECT c FROM Control c WHERE c.id = :id", Control.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    // 프로덕트 이름으로 찾기
    public Control findByProductName(ProductName productName) {
        try {
            return em.createQuery("SELECT c FROM Control c WHERE KEY(c.processRunningStatus) IN :productNames", Control.class)
                    .setParameter("productNames", productName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // 그룹명을 Control 엔티티를 찾는 메서드
    public List<Control> findByGroupname(String groupname) {
        return em.createQuery("SELECT c FROM Control c WHERE c.groupname = :groupname", Control.class)
                .setParameter("groupname", groupname)
                .getResultList();
    }

    // 그룹명 리스트 반환하는 메서드
    public List<String> findGroupNames() {
        // 고정된 그룹 이름 리스트를 반환하
        List<String> groupNames = new ArrayList<>();
        groupNames.add("H");
        groupNames.add("B");
        groupNames.add("J");
        // 필요한 만큼 그룹 이름을 추가할 수 있습니다.
        return groupNames;
    }

    public void delete(Control control) {
        em.remove(control);
    }

    public List<Control> findAll() {
        return em.createQuery("SELECT c FROM Control c", Control.class).getResultList();
    }

    // 그룹 정보를 저장하는 메서드 수정
    @Transactional
    public void saveGroupInfo(Control control, Map<ProcessName, Boolean> processRunningStatus) {
        // Control 엔티티가 영속 상태인지 확인
        if (!em.contains(control)) {
            // 영속 상태가 아니라면, 엔티티를 영속 상태로 만듦
            control = em.merge(control);
        }
        // Control 엔티티에 프로세스 이름 맵 설정
        control.setProcessRunningStatus(processRunningStatus);
        // 변경된 Control 엔티티를 저장
        em.persist(control);
    }
}