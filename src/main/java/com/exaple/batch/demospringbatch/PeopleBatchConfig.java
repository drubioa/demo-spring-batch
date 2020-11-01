package com.exaple.batch.demospringbatch;

import com.exaple.batch.demospringbatch.model.People;
import com.exaple.batch.demospringbatch.services.CalculateAgeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
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
    private CalculateAgeService calculateAgeService;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;

    private AtomicInteger batchRunCounter = new AtomicInteger(0);

    /* JpaPagingItemReader */
    @Bean
    public ItemReader<People> itemReader() {
        val jpqlQuery = "select p from People p";
        return new JpaPagingItemReaderBuilder<People>()
                .name("itemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .pageSize(5)
                .build();
    }

    /* Reusing Existing Services */
    @Bean
    public ItemWriter<People> itemWriter() {
        ItemWriterAdapter<People> reader = new ItemWriterAdapter<>();
        reader.setTargetObject(calculateAgeService);
        reader.setTargetMethod("recalculateAge");
        return reader;
    }

    @Bean
    public Step step1(ItemReader<People> reader, ItemWriter<People> writer) {
        return stepBuilderFactory.get("step1")
                .<People, People> chunk(10)
                .reader(reader)
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
        /*JobExecution jobExecution = jobLauncher
                    .run(incrementAgePeopleJob(step1(itemReader(), itemWriter())),
                            new JobParametersBuilder()
                            .addDate("launchDate", new Date())
                            .toJobParameters());*/
        batchRunCounter.incrementAndGet();
    }

}
