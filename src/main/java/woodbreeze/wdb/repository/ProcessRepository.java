package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.service.ProcessService;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProcessRepository {

    private final EntityManager em;


    //저장
    public void save(Process process){
        if(process.getId() == null) {
            em.persist(process);
        } else {
            em.merge(process);
        }
    }

    //단건조회
    public Process findById(Long id) { return em.find(Process.class, id);}

    public List<Process> findAll(){
        return em.createQuery("select p from Process p", Process.class).getResultList();
    }

    public List<Process> findProcess(Long id){
        return em.createQuery("select p from Process p", Process.class).getResultList();
    }


}
