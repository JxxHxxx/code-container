package com.example.demo.batch.job.processor;

import com.example.demo.pay.domain.Pay;

import com.example.demo.pay.domain.PayStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaySummaryItemProcessor extends ItemStreamSupport implements ItemProcessor<Pay, Pay> {

    /**
     * key : pay_id
     * value : skip 사유
     */
    private Map<Long, String> skippedItems = new HashMap<>();

    @Override
    public void open(ExecutionContext executionContext) {
        log.info("executionContext init");
        this.skippedItems = (Map<Long, String>) executionContext.get("skippedItems");

        if (this.skippedItems == null) {
            skippedItems = new HashMap<>();
            executionContext.put("skippedItems", skippedItems);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        log.info("update executionContext");
        Map<Long, String> skippedItemsInExecutionContext = (Map<Long, String>) executionContext.get("skippedItems");
        HashMap<Long, String> copyMap = new HashMap<>();

        for (Long key : skippedItems.keySet()) {
            log.info("key : {} value : {}", key, skippedItems.get(key));
            copyMap.put(key, skippedItems.get(key));
        }

        skippedItemsInExecutionContext.putAll(copyMap);
    }

    @Override
    public Pay process(Pay pay) throws Exception {
        if (!pay.isCompletedPayment()) {
            PayStatus payStatus = pay.getPayStatus();
            log.info("IS NOT PAYMENT_COMPLETED STATUS : PAY ITEM STATUS IS : {} PAY ID : {}", payStatus, pay.getId());

            // 한글 넣지 말자.
            skippedItems.put(pay.getId(), pay.getPayStatus().name());
            throw new UnableToProcessException("IS NOT PAYMENT_COMPLETED STATUS : PAY ITEM STATUS IS " + payStatus.name());
        }

        return pay;
    }
}
