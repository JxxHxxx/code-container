package com.example.demo.batch.mapper;

import com.example.demo.pay.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * rowMapper 에서는 resultSet 에 필드가 DB의 컬럼명을 따라간다.
 * 반면 FieldSetMapper 에서는 이러한 작업을 하지 않는다. 해당 작업은 앞단의 LineTokenizer 에서 한다.
 *
 */

@Component
public class PayFieldSetMapper implements FieldSetMapper<Pay> {

    @Override
    public Pay mapFieldSet(FieldSet fieldSet) throws BindException {
        PayDto payDto = create(fieldSet);

        OrderInformation orderInformation = new OrderInformation(payDto.orderNo, payDto.orderAmount, payDto.storeId);
        PayInformation payInformation = new PayInformation(payDto.payerId, payDto.payAmount, payDto.vatAmount, payDto.payType, payDto.payStatus);

        return new Pay(orderInformation, payInformation, payDto.createdDate, payDto.createdTime);
    }

    private PayDto create(FieldSet fieldSet) {
        LocalDate createdDate = null;
        LocalTime createdTime = null;
        int orderAmount = 0;
        String orderNo = null;
        String storeId = null;
        int payAmount = 0;
        PayStatus payStatus = null;
        PayType payType = null;
        String payerId = null;
        int vatAmount = 0;
        try {
            createdDate = LocalDate.parse(fieldSet.readString("created_date"));
            createdTime = LocalTime.parse(fieldSet.readString("created_time"));
            orderAmount = fieldSet.readInt("order_amount");
            orderNo = fieldSet.readString("order_no");
            storeId = fieldSet.readString("store_id");
            payAmount = fieldSet.readInt("pay_amount");
            payStatus = PayStatus.valueOf(fieldSet.readString("pay_status"));
            payType = PayType.valueOf(fieldSet.readString("pay_type"));
            payerId = fieldSet.readString("payer_id");
            vatAmount = fieldSet.readInt("vat_amount");
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
