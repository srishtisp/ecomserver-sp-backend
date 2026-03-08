package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inbox_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String customerName;

    private Long reviewId;
    private Long productId;

    private String title;

    @Column(length = 1000)
    private String message;

    private String createdByAdmin;
    private LocalDateTime createdAt;
    private boolean read;
}
