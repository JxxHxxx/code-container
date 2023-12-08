package com.example.demo;

import com.example.demo.pay.domain.*;
import com.example.demo.pay.dto.PayForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class SimpleRowMapper implements FieldSetMapper<Pay> {

    @Override
    public Pay mapFieldSet(FieldSet fieldSet) throws BindException {

        PayDto payDto = create(fieldSet.getValues());

        OrderInformation orderInformation = new OrderInformation(payDto.orderNo, payDto.orderAmount, payDto.storeId);
        PayInformation payInformation = new PayInformation(payDto.payerId, payDto.payAmount, payDto.vatAmount, payDto.payType, payDto.payStatus);

        return new Pay(orderInformation, payInformation, payDto.createdDate, payDto.createdTime);
    }

    private PayDto create(String[] oneLine) {
        LocalDate createdDate = LocalDate.parse(oneLine[1]);
        LocalTime createdTime = LocalTime.parse(oneLine[2]);
        Integer orderAmount = Integer.valueOf(oneLine[3]);
        String orderNo = oneLine[4];
        String storeId = oneLine[5];
        Integer payAmount = Integer.valueOf(oneLine[6]);
        PayStatus payStatus = PayStatus.valueOf(oneLine[7]);
        PayType payType = PayType.valueOf(oneLine[8]);
        String payerId = String.valueOf(oneLine[9]);
        Integer vatAmount = Integer.valueOf(oneLine[10]);

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
