package com.example.demo.pay.presentation;

import com.example.demo.pay.application.PayService;
import com.example.demo.pay.dto.PayRequest;
import com.example.demo.pay.dto.PayResult;
import com.example.demo.pay.dto.PayServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PayApiController {

    private final PayService payService;

    @PostMapping("/pays")
    public ResponseEntity<PayResult<PayServiceResponse>> createPay(@RequestBody PayRequest payRequest) {
        PayServiceResponse response = payService.save(payRequest);

        return ResponseEntity.ok(new PayResult<PayServiceResponse>(HttpStatus.OK.value(), "결제 생성 완료", response));
    }


}
