package com.example.demo.message.infra;

import com.example.demo.message.domain.MessageStatus;
import com.example.demo.message.domain.QMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QMessageHistoryRepository extends JpaRepository<QMessageHistory, Long> {

    @Query(value = "SELECT qm FROM QMessageHistory qm " +
            "WHERE qm.messageStatus =:messageStatus ")
    List<QMessageHistory> readBy(@Param("messageStatus") MessageStatus messageStatus);

    List<QMessageHistory> findQMessageHistoriesByMessageStatus(MessageStatus messageStatus);
}
