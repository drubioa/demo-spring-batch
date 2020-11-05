package com.exaple.batch.demospringbatch;

import com.exaple.batch.demospringbatch.item.PeopleItemProcessor;
import com.exaple.batch.demospringbatch.model.Book;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
@EnableAutoConfiguration()
public class PeopleBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private JobLauncher jobLauncher;

    private AtomicInteger batchRunCounter = new AtomicInteger(0);

    /* JpaPagingItemReader */
    @Bean
    @StepScope
    public JpaPagingItemReader<Book> itemReader() {
        val jpqlQuery = "select p from Book p";
        JpaPagingItemReader itemReader = new JpaPagingItemReaderBuilder<Book>()
                .name("itemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .pageSize(1)
                .build();
        return itemReader;
    }

    /* JpaItemWriter */
    @Bean
    @StepScope
    public JpaItemWriter<Book> itemWriter() {
        JpaItemWriter<Book> employeeJpaItemWriter = new JpaItemWriter<>();
        employeeJpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return employeeJpaItemWriter;
    }

    @Bean
    @StepScope
    public ItemProcessor<Book, Book> itemProcessor() {
        return new PeopleItemProcessor();
    }

    @Bean
    public Step step1(@Qualifier("itemReader") ItemReader<Book> reader,
                      @Qualifier("itemProcessor") ItemProcessor<Book, Book> processor,
                      @Qualifier("itemWriter") ItemWriter<Book> writer) {
        return stepBuilderFactory.get("step1")
                .<Book, Book> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job incrementAgePeopleJob(Step step1) {
        log.debug("Init job 'incrementAgePeopleJob'");
        return jobBuilderFactory.get("incrementAgePeopleJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Scheduled(fixedRate = 30_000L)
    public void launchJob() throws Exception {
        log.info("launch Job 'incrementAgePeopleJob'");
        JobExecution jobExecution = jobLauncher
                    .run(incrementAgePeopleJob(step1(itemReader(), itemProcessor(), itemWriter())),
                            new JobParametersBuilder()
                            .addDate("launchDate", new Date())
                            .toJobParameters());
        batchRunCounter.incrementAndGet();
    }

}
