package com.example.mhbc.entity;

import com.example.mhbc.dto.HallDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HALL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx; // 홀 번호

  private String name; // 홀 이름

  @Builder.Default
  private Integer capacity = 0; // 수용 인원

  @Builder.Default
  private Integer price = 0; // 가격

  public HallDTO toDTO() {
    return HallDTO.builder()
      .idx(idx)
      .name(name)
      .capacity(capacity)
      .price(price)
      .build();
  }
}
