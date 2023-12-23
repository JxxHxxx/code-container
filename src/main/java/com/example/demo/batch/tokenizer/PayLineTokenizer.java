package com.example.demo.batch.tokenizer;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.stereotype.Component;

@Component
public class PayLineTokenizer implements LineTokenizer {

    private final String delimiter = ",";
    private final String[] names = new String[]{"pay_id", "created_date", "created_time", "order_amount", "order_no",
            "store_id", "pay_amount", "pay_status", "pay_type", "payer_id", "vat_amount"};

    @Override
    public FieldSet tokenize(String line) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        lineTokenizer.setNames(names);

        return lineTokenizer.tokenize(line);
    }
}
