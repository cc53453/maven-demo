package io.github.cc53453.springbatch.test;

import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import io.github.cc53453.MainWithDatasource;
import io.github.cc53453.datatype.util.DateHelper;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = MainWithDatasource.class)
@SpringBatchTest
@Slf4j
class JobWithMockReaderTest {
    @Autowired
    JobLauncherTestUtils jobLauncher;

    @MockitoBean
    @Qualifier("demoReader")
    JdbcCursorItemReader<String> reader;
    
    @Test
    void test() throws Exception  {
        // 跑批传参
        JobParameters params = new JobParametersBuilder()
                .addString("date", DateHelper.now(DateHelper.FORMAT_YYYY_MM_DD_HH_MM_SS))
                .toJobParameters();
        
        // mock reader
        AtomicInteger index = new AtomicInteger(0);
        // mock read() 方法，每次返回数组中的一个元素，读完返回 null
        when(reader.read()).thenAnswer(invocation -> {
            int i = index.getAndIncrement();
            if(i<50) {
                return "a".concat(String.valueOf(i));
            }
            return null;
        });
        JobExecution execution = jobLauncher.launchStep("demoStep", params);
        Assertions.assertEquals(BatchStatus.COMPLETED, execution.getStatus());
    }
}
