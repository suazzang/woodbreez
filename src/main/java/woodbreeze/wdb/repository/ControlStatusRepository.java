package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.ControlStatus;
import woodbreeze.wdb.domain.Member;
import woodbreeze.wdb.domain.Product;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ControlStatusRepository {
    private final EntityManager em;


    // 저장
    public void save(ControlStatus controlStatus) {
        if (controlStatus.getId() == null) {
            em.persist(controlStatus); // 새로운 원자재인 경우
        } else {
            em.merge(controlStatus); // 기존의 원자재인 경우
        }
    }

    // 업데이트
    public void update(ControlStatus controlStatus) {
            em.merge(controlStatus);
        }

    //단건조회
    public ControlStatus findOne(Long id){
        return em.find(ControlStatus.class, id);
    }
    //모두조회
    public List<ControlStatus> findAll(){
        return em.createQuery("select c from  ControlStatus c",  ControlStatus.class).getResultList();
    }


    public boolean isRunning() {
        List<ControlStatus> controlStatusList = em.createQuery("SELECT c FROM ControlStatus c", ControlStatus.class)
                .getResultList();
        if (!controlStatusList.isEmpty()) {
            return controlStatusList.get(0).isRunning();
        } else {
            // 공정 상태가 존재하지 않는 경우 기본적으로 실행 중이 아니라고 가정합니다.
            return false;
        }
    }
}