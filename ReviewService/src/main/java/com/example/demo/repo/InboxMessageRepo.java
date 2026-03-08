package com.example.demo.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.InboxMessage;

public interface InboxMessageRepo extends JpaRepository<InboxMessage, Long> {
    List<InboxMessage> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
