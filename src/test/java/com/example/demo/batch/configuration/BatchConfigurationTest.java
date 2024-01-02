package com.example.demo.batch.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BatchConfigurationTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void test() {
        sqlSessionFactory.openSession();
    }

}