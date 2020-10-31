package com.exaple.batch.demospringbatch;

import com.exaple.batch.demospringbatch.dto.People;
import lombok.val;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<People> jpaPagingItemReader() {
        val jpqlQuery = "select p from People p";
        JpaPagingItemReader<People> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(jpqlQuery);
        reader.setPageSize(10);
        return reader;
    }


}
