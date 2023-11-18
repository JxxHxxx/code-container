package com.example.demo.sales.infra;

import com.example.demo.sales.SalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesSummaryRepository extends JpaRepository<SalesSummary, String> {
}
