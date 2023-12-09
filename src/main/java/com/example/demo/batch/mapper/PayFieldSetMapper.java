package com.example.demo.batch.mapper;

import com.example.demo.pay.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class PayFieldSetMapper implements FieldSetMapper<Pay> {

    @Override
    public Pay mapFieldSet(FieldSet fieldSet) throws BindException {
        PayDto payDto = create(fieldSet.getValues());

        OrderInformation orderInformation = new OrderInformation(payDto.orderNo, payDto.orderAmount, payDto.storeId);
        PayInformation payInformation = new PayInformation(payDto.payerId, payDto.payAmount, payDto.vatAmount, payDto.payType, payDto.payStatus);

        return new Pay(orderInformation, payInformation, payDto.createdDate, payDto.createdTime);
    }

    private PayDto create(String[] oneLine) {
        LocalDate createdDate = null;
        LocalTime createdTime = null;
        Integer orderAmount = null;
        String orderNo = null;
        String storeId = null;
        Integer payAmount = null;
        PayStatus payStatus = null;
        PayType payType = null;
        String payerId = null;
        Integer vatAmount = null;
        try {
            createdDate = LocalDate.parse(oneLine[1]);
            createdTime = LocalTime.parse(oneLine[2]);
            orderAmount = Integer.valueOf(oneLine[3]);
            orderNo = oneLine[4];
            storeId = oneLine[5];
            payAmount = Integer.valueOf(oneLine[6]);
            payStatus = PayStatus.valueOf(oneLine[7]);
            payType = PayType.valueOf(oneLine[8]);
            payerId = String.valueOf(oneLine[9]);
            vatAmount = Integer.valueOf(oneLine[10]);
        } catch (IllegalArgumentException e) {
            throw new BatchExecuteException("형변환 할 수 없음");
        }

        return new PayDto(createdDate, createdTime, orderAmount, orderNo, storeId, payAmount, payStatus, payType, payerId, vatAmount);
    }

    @RequiredArgsConstructor
    private class PayDto {
        private final LocalDate createdDate;
        private final LocalTime createdTime;
        private final Integer orderAmount;
        private final String orderNo;
        private final String storeId;
        private final Integer payAmount;
        private final PayStatus payStatus;
        private final PayType payType;
        private final String payerId;
        private final Integer vatAmount;
    }

}
