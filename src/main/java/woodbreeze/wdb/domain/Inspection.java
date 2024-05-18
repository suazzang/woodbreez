package woodbreeze.wdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Inspection {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //식별자

    private Long processId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Error error;
    private String errorMessage;


    @Enumerated(EnumType.STRING)
    private InspectionStatus result;    // 검수안에 결과를 저장

}
