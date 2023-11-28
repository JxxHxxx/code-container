package com.example.demo.sales.infra;

import com.example.demo.sales.SalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SalesSummaryRepository extends JpaRepository<SalesSummary, String> {

    Optional<SalesSummary> findByStoreIdAndSalesDate(String storeId, LocalDate salesDate);
}
