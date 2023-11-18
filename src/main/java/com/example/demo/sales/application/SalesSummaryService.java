package com.example.demo.sales.application;

import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.dto.SalesSummaryForm;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesSummaryService {

    private final SalesSummaryRepository salesSummaryRepository;

    @Transactional
    public void saveAll(List<SalesSummaryForm> forms) {
        List<SalesSummary> salesSummaries = forms.stream().map(form -> new SalesSummary(
                        form.getStoreId(),
                        form.getDailyTotalSales(),
                        form.getDailyVatDeductedSales(),
                        form.getCreateSystem()
                )
        ).collect(Collectors.toList());

        salesSummaryRepository.saveAll(salesSummaries);
    }
}
