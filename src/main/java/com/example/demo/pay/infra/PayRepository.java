package com.example.demo.pay.infra;

import com.example.demo.pay.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Long> {

    @Query("select distinct p.storeId from Pay p")
    List<String> findAllStore();

    int countByStoreId(String storeId);
    List<Pay> findByStoreId(String storeId);

    //mysql
    @Query("select p from Pay p " +
            "where p.storeId =:storeId " +
            "and substring(p.createTime, 1,10) =:requestDate")
    List<Pay> findOneDayPays(@Param("storeId") String storeId, @Param("requestDate") String requestDate);

    //mssql
    @Query(value = "select p from Pay p " +
            "where p.storeId =:storeId " +
            "and CONVERT(DATE, p.createTime, 120) =:requestDate")
    List<Pay> findOneDayPaysN(@Param("storeId") String storeId, @Param("requestDate") String requestDate);
}
