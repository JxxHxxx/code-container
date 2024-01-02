package com.example.demo.admin.infra;

public class JobExecutionMapper {
    protected static final String JOB_EXECUTIONS_WITH_JOB_NAME_SQL = "SELECT " +
            "bje.JOB_EXECUTION_ID, " +
            "bji.JOB_NAME, " +
            "bje.JOB_INSTANCE_ID, " +
            "bje.START_TIME, " +
            "bje.END_TIME, " +
            "bje.STATUS, " +
            "bje.EXIT_CODE " +
            "FROM BATCH_JOB_EXECUTION bje with(nolock) " +
            "JOIN BATCH_JOB_INSTANCE bji ON bje.JOB_INSTANCE_ID  = bji.JOB_INSTANCE_ID " +
            "WHERE bje.START_TIME >= ? " +
            "AND bje.END_TIME <= ? ";

    protected static final String UN_SUCCESSFUL_JOB_EXECUTIONS_SQL = "SELECT " +
            "bje.JOB_EXECUTION_ID, " +
            "bji.JOB_NAME, " +
            "bje.JOB_INSTANCE_ID, " +
            "bje.START_TIME, " +
            "bje.END_TIME, " +
            "bje.STATUS, " +
            "bje.EXIT_CODE " +
            "FROM BATCH_JOB_EXECUTION bje with(nolock) " +
            "JOIN BATCH_JOB_INSTANCE bji ON bje.JOB_INSTANCE_ID  = bji.JOB_INSTANCE_ID " +
            "WHERE bje.EXIT_CODE IN ('UNKNOWN', 'ABANDONED', 'FAILED', 'STOPPED', 'STOPPING')";

    protected static final String SUCCESSFUL_EXECUTIONS_SQL = "SELECT " +
            "bje.JOB_EXECUTION_ID, " +
            "bji.JOB_NAME, " +
            "bje.JOB_INSTANCE_ID, " +
            "bje.START_TIME, " +
            "bje.END_TIME, " +
            "bje.STATUS, " +
            "bje.EXIT_CODE " +
            "FROM BATCH_JOB_EXECUTION bje with(nolock) " +
            "JOIN BATCH_JOB_INSTANCE bji ON bje.JOB_INSTANCE_ID  = bji.JOB_INSTANCE_ID " +
            "WHERE bje.EXIT_CODE = 'COMPLETED'";
}
