package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable=false)
    private String productName;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false, precision = 12, scale = 2)
    private BigDecimal price;

    // ✅ store vendor info without join
    @Column(nullable=false)
    private Long vendorId;

    @Column(nullable=false)
    private String vendorName;

    @Column(nullable=false)
    private String category;

    @Column(nullable=false)
    private boolean active = true;
}