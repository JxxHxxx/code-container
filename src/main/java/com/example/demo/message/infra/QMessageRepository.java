package com.example.demo.message.infra;

import com.example.demo.message.domain.QMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QMessageRepository extends JpaRepository<QMessage, Long> {
}
