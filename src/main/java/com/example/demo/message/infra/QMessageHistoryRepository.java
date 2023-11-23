package com.example.demo.message.infra;

import com.example.demo.message.domain.QMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QMessageHistoryRepository extends JpaRepository<QMessageHistory, Long> {
}
