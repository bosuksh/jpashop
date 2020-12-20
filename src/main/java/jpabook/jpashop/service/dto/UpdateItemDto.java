package jpabook.jpashop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UpdateItemDto {
  private String name;
  private int price;
  private int stockQuantity;
}
