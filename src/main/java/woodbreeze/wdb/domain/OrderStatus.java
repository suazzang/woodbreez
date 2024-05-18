package woodbreeze.wdb.domain;

public enum OrderStatus {
    CREATED, //: 주문 생성, 공정 들어가기 전 상태
    PROCESSING,// 주문 처리중
    COMPLETED, // 주문 처리 완료
    CANCELLED //주문 취소
}
