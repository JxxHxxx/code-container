package com.example.demo.admin.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobExecutionRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String JOB_EXECUTIONS_WITH_JOB_NAME_SQL = "SELECT " +
            "bje.JOB_EXECUTION_ID, " +
            "bji.JOB_NAME, " +
            "bje.JOB_INSTANCE_ID, " +
            "bje.START_TIME, " +
            "bje.END_TIME, " +
            "bje.STATUS, " +
            "bje.EXIT_CODE " +
            "FROM BATCH_JOB_EXECUTION bje with(nolock) " +
            "JOIN BATCH_JOB_INSTANCE bji ON bje.JOB_INSTANCE_ID  = bji.JOB_INSTANCE_ID " +
            "WHERE bje.START_TIME >= ? + ' 00:00:00.000' " +
            "AND bje.END_TIME <= ? + ' 23:59:59.599'";


    public List<CustomJobExecution> getJobExecutionsWithJobNameByStartDate(String start, @Nullable String end) {
        if (Objects.isNull(end) || end.isEmpty()) {
            end = start;
        }

        BeanPropertyRowMapper<CustomJobExecution> rowMapper = new BeanPropertyRowMapper<>(CustomJobExecution.class);
        return jdbcTemplate.query(JOB_EXECUTIONS_WITH_JOB_NAME_SQL, rowMapper, start, end);
    }
}
