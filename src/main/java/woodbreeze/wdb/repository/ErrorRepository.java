package woodbreeze.wdb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woodbreeze.wdb.domain.Error;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ErrorRepository {

    private final EntityManager em;

    // 에러 저장
    @Transactional
    public void saveError(Error error) {
        em.persist(error);
    }

    // 모든 에러 조회
    public List<Error> findAllErrors() {
        String jpql = "SELECT e FROM Error e";
        TypedQuery<Error> query = em.createQuery(jpql, Error.class);
        return query.getResultList();
    }

    public Error findById(Long id) {
        return em.find(Error.class, id);
    }

//    // 특정 에러 종류 조회
//    public List<Error> findErrorsByType(String errorType) {
//        String jpql = "SELECT e FROM Error e WHERE e.errorCode LIKE :errorType%";
//        TypedQuery<Error> query = em.createQuery(jpql, Error.class);
//        query.setParameter("errorType", errorType);
//        return query.getResultList();
//    }
}
