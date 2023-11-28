package com.example.demo.pay.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TempStoreRepository extends JpaRepository<TempStore, Long> {


    @Query(value = "DELETE FROM temp_store t ORDER BY t.id " +
            "       LIMIT 1 ", nativeQuery = true)
    void deleteTopOne();

    @Query(value = "SELECT t.store_id FROM temp_store t " +
            "       ORDER BY id " +
            "       ASC LIMIT 1", nativeQuery = true)
    String selectOneStoreId();

    @Query(value = "SELECT t.store_id FROM temp_store t ", nativeQuery = true)
    List<String> selectAllStoreId();

}
