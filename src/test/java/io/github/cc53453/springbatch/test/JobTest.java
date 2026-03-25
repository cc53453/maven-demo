package io.github.cc53453.springbatch.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.MainWithDatasource;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = MainWithDatasource.class)
@SpringBatchTest
@Slf4j
class JobTest {
    @Autowired
    JobLauncherTestUtils jobLauncher;
    
    @Test
    void test() throws Exception  {
        JobExecution execution = jobLauncher.launchJob();
        Assertions.assertEquals(BatchStatus.COMPLETED, execution.getStatus());
    }
}
