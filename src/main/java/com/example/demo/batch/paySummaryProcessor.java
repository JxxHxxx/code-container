package com.example.demo.batch;

import com.example.demo.pay.domain.Pay;
import com.example.demo.sales.SalesSummary;
import com.example.demo.sales.dto.PayDto;
import com.example.demo.sales.infra.SalesSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.demo.sales.SystemType.BATCH;

/**
 * 여기서는 환불된 OrderNo 제외하는 작업이 일어나야 함
 */

@Component
@RequiredArgsConstructor
public class paySummaryProcessor implements ItemProcessor<Pay, SalesSummary> {

    private final SalesSummaryRepository salesSummaryRepository;

    @Override
    public SalesSummary process(Pay item) throws Exception {
        Optional<SalesSummary> optionalSalesSummary = salesSummaryRepository.findByStoreIdAndSalesDate(item.getStoreId(), item.getCreatedDate());

        PayDto payDto = new PayDto(item.getStoreId(), item.getPayAmount(), item.getVatAmount(), item.getCreatedDate());
        if (optionalSalesSummary.isPresent()) {

            SalesSummary findSaleSummary = optionalSalesSummary.get();
            findSaleSummary.reflectPayInformation(payDto);

        }
        else {
            SalesSummary salesSummary = SalesSummary.constructorStoreIdIsNotExistCase(payDto, BATCH);
            salesSummaryRepository.save(salesSummary);
        }
        return null;
    }
}
