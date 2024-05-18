package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Error {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String errorCode;   // 에러코드

    private String errorMessage;   // 에러 메세지

    private String errorName;   // 에러 이름

    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

   @OneToMany(mappedBy = "error")
   private List<Inspection> inspections = new ArrayList<>();

}
