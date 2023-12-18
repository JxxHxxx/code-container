package com.example.demo.pay;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class PayTest {

    @Test
    void getTime() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }

    @Test
    void lastIndexOf() {
        String oldDn = "CN=DCC20131026,OU=경영진단팀,OU=준법경영실,OU=DL건설,DC=DLCON,DC=CO,DC=KR";
        int lastOUIndex = oldDn.lastIndexOf("OU"); // POU <- U의 인덱스 위치를 반환
        String topParentOuDn = oldDn.substring(lastOUIndex);
        String cn = oldDn.split(",")[0].trim();
        String newDn = cn.concat(",").concat(topParentOuDn);

        System.out.println("=============================================");
        System.out.println("topParentOuDn = " + topParentOuDn);
        System.out.println("cn = " + cn);
        System.out.println("oldDn = " + oldDn);
        System.out.println("newDn = " + newDn);
        System.out.println("=============================================");

        String testWord = "123    ";
        System.out.println(testWord + "]");
        String afterTestWor = testWord.replaceAll(" ", "");
        System.out.println(afterTestWor + "]");
    }
}