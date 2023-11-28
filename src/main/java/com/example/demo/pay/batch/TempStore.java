package com.example.demo.pay.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class TempStore {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeId;

    public TempStore(String storeId) {
        this.storeId = storeId;
    }
}
