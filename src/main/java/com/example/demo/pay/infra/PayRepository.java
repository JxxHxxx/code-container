package com.example.demo.pay.infra;

import com.example.demo.pay.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PayRepository extends JpaRepository<Pay, Long> {

    @Query("select distinct p.receiverId from Pay p")
    List<String> findAllReceiver();

    List<Pay> findByReceiverId(String receiverId);
}
