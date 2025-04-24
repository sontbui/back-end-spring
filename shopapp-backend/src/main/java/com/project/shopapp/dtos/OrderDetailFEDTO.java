package com.project.shopapp.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailFEDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String thumbnail;
    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;
}