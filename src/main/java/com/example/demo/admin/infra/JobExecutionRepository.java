package com.example.demo.admin.infra;

import com.example.demo.admin.dto.response.JobExecutionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.admin.infra.JobExecutionMapper.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobExecutionRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<JobExecutionResponse> findJobExecutionsByDuration(LocalDateTime startDateTime, @Nullable LocalDateTime endDateTime) {
        BeanPropertyRowMapper<JobExecutionResponse> rowMapper = new BeanPropertyRowMapper<>(JobExecutionResponse.class);
        return jdbcTemplate.query(JOB_EXECUTIONS_WITH_JOB_NAME_SQL, rowMapper, startDateTime, endDateTime);
    }

    public List<JobExecutionResponse> findJobExecutionsByExitCode(boolean isSuccessful) {
        String sql = null;
        BeanPropertyRowMapper<JobExecutionResponse> rowMapper = new BeanPropertyRowMapper<>(JobExecutionResponse.class);

        if (isSuccessful) {
            return jdbcTemplate.query(SUCCESSFUL_EXECUTIONS_SQL, rowMapper);
        }

        else  {
            return jdbcTemplate.query(UN_SUCCESSFUL_JOB_EXECUTIONS_SQL, rowMapper);
        }
    }
}
