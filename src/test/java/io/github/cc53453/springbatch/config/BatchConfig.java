package io.github.cc53453.springbatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import io.github.cc53453.springbatch.exception.MyException1;
import io.github.cc53453.springbatch.exception.MyException2;
import io.github.cc53453.springbatch.exception.MyException3;
import lombok.extern.slf4j.Slf4j;

/**
 * spring batch使用案例。
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {

    @Bean(name="demoReader")
    JdbcCursorItemReader<String> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<String>()
                .name("demoDbReader")
                .dataSource(dataSource)
                .sql("SELECT name FROM demo")
                .rowMapper((rs, rowNum) -> {
                    return rs.getString(1);
                })
                .build();
    }

    @Bean(name="demoProcessor")
    ItemProcessor<String, String> processor() {
        return name->{
            if("alwaysskip".equals(name)) {
                log.error("always throw MyException1");
                throw new MyException1("always skip");
            }
            
            Double d = Math.random();
            if(d < 0.1) {
                // 模拟有10%概率出现异常1
                log.error("MyException1: {}", name);
                throw new MyException1("10% error");
            }
            if(d >= 0.1 && d < 0.3) {
                // 模拟有20%概率出现异常2
                log.error("MyException2: {}", name);
                throw new MyException2("20% error");
            }
            if(d >= 0.3 && d < 0.45) {
                // 模拟有15%概率出现异常3
                log.error("MyException3: {}", name);
                throw new MyException3("15% error");
            }
            return name.toUpperCase();
        };
    }

    @Bean(name="demoWriter")
    ItemWriter<String> writer() {
        return name -> {
            log.info("writer: {}", name);
        };
    }
    
    @Bean(name="demoListener")
    SkipListener<String, String> demoListener() {
        return new SkipListener<>() {
            @Override
            public void onSkipInProcess(String item, Throwable t) {
                // 打印出来，或者入库，方便补批
                log.error("skip: {}, because: {}", item, t.getMessage());
            }
        };
    }

    @Bean(name="demoStep")
    Step demoStep(JobRepository jobRepository, 
                      JdbcTransactionManager transactionManager, 
                      @Qualifier("demoReader") JdbcCursorItemReader<String> reader,
                      @Qualifier("demoProcessor") ItemProcessor<String, String> processor,
                      @Qualifier("demoWriter") ItemWriter<String> writer, 
                      @Qualifier("demoListener")SkipListener<String, String> demoListener) {
        return new StepBuilder("demoStep", jobRepository)
                // 每2条数据（item）一个事务
                .<String, String>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                
                // 开始配置容错
                .faultTolerant()
                
                // skip监听
                .listener(demoListener)
                
                // 异常1可以跳过不可以重试
                // 异常2先重试，不行跳过
                // 异常3先重试，不行也不能跳过
                .skip(MyException1.class)
                .skip(MyException2.class)
                .noSkip(MyException3.class)
                .noRetry(MyException1.class)
                .retry(MyException2.class)
                .retry(MyException3.class)
                
                // 最多跳过100个item
                .skipLimit(100)
                
                // 每个item最多重试50次
                .retryLimit(50)
                
                // 重试的间隔配置
                .backOffPolicy(new ExponentialBackOffPolicy() {{
                    setInitialInterval(1000L); // 第一次等1s
                    setMultiplier(2.0);  // 每次翻倍等待次数，第二次2s，第三次4s，第四次8s
                    setMaxInterval(10000L); // 最久等待10s，所以第五次是10s而非16s
                }})
                .build();
    }

    @Bean(name="demoJob")
    Job demoJob(JobRepository jobRepository, @Qualifier("demoStep") Step step1) {
        return new JobBuilder("demoJob", jobRepository)
                .start(step1)
                .build();
    }
}