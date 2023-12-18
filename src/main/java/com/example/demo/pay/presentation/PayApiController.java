package com.example.demo.pay.presentation;

import com.example.demo.pay.application.PayService;
import com.example.demo.pay.domain.Pay;
import com.example.demo.pay.dto.PayForm;
import com.example.demo.pay.dto.PayResult;
import com.example.demo.pay.dto.PayServiceResponse;
import com.example.demo.pay.dto.RefundServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PayApiController {

    private final PayService payService;
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/pays")
    public ResponseEntity<PayResult<PayServiceResponse>> createPay(@RequestBody PayForm payForm) {
        PayServiceResponse response = payService.save(payForm);

        return ResponseEntity.ok(new PayResult<PayServiceResponse>(HttpStatus.OK.value(), "결제 생성 완료", response));
    }

    @PostMapping("/pays/{iteration}")
    public String createPays(@PathVariable("iteration") int iteration) {
        payService.asyncSaveAll(iteration);
        return "다건 생성 완료";
    }

    @PostMapping("/pays/{pay-id}/refund")
    public ResponseEntity<PayResult<RefundServiceResponse>> refundPay(@PathVariable(name = "pay-id") Long payId) {
        // TODO : 주문 서버에서 주문 상태 API 개발해둬야 함
//        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
//        ResponseEntity<Integer> result = restTemplate.exchange("주문 서버 - 주문 상태 URI", HttpMethod.GET, entity, Integer.class);
//        Integer orderStatus = result.getBody();

        int orderStatus = new Random().nextInt(5);
        log.info("orderStatus : {}", orderStatus);

        RefundServiceResponse response = payService.tryRefund(payId, orderStatus);

        return ResponseEntity.ok(new PayResult<>(200, "환불 완료", response));
    }
}
