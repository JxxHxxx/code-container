package com.example.demo.pay.infra;

import com.example.demo.pay.domain.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long> {
}
