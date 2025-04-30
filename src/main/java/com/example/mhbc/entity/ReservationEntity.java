package com.example.mhbc.entity;

import com.example.mhbc.dto.ReservationDTO;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "RESERVATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // 예약번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_IDX")
    private MemberEntity member; // 회원

    private String name; // 예약자 성함
    private String eventType; // 행사 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HALL_IDX")
    private HallEntity hall; // 홀

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_DATE")
    private Date eventDate; // 행사 예정일

    private Integer guestCnt; // 행사 인원수
    private String mealType; // 식사 종류
    private String flower; // 꽃장식
    private String contactTime; // 연락 가능한 시간
    private String note; // 기타 문의
    private String userComment; // 코멘트

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_AT")
    private Date createdAt; // 작성일

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_AT")
    private Date updatedAt; // 수정일

    private String status; // 예약 상태
    private Integer totalAmount; // 총금액

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public ReservationDTO toDTO() {
        return ReservationDTO.builder()
            .name(name)
            .eventType(eventType)
            .eventDate(eventDate)
            .guestCnt(guestCnt)
            .mealType(mealType)
            .flower(flower)
            .contactTime(contactTime)
            .note(note)
            .userComment(userComment)
            .status(status)
            .totalAmount(totalAmount)
            .createdAt(createdAt)
            .memberIdx(member != null ? member.getIdx() : null)
            .hallIdx(hall != null ? hall.getIdx() : null)
            .build();
    }
}
