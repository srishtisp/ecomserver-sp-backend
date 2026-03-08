package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    name = "reviews",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productId", "customerId"})
    }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Long productId;

    private Long customerId;   // must match security service

    private String customerName;

    @Min(1)
    @Max(5)
    private int rating;

    private String comment;

    private LocalDateTime createdAt;
}