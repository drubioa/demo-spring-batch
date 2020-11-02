package com.example.batch.demospringbatch;

import com.exaple.batch.demospringbatch.DemoSpringBatchApplication;
import com.exaple.batch.demospringbatch.model.People;
import com.exaple.batch.demospringbatch.repository.PeopleRepository;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { DemoSpringBatchApplication.class })
public class PeopleBatchConfigTest {

    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    private final Long EXAMPLE_ID = 1L;

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void step1_sucessfully() throws Exception {

        //given:
        People p = new People();
        p.setId(EXAMPLE_ID);
        p.setName("Anonymous");
        p.setAge(18);
        peopleRepository.save(p);
        val initialAge = peopleRepository.findById(EXAMPLE_ID).get().getAge();

        //when:
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");
        Collection actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        
        //then:
        assertEquals(actualStepExecutions.size(), 1);
        assertEquals(actualJobExitStatus.getExitCode(), "COMPLETED");
        val afterAge = peopleRepository.findById(EXAMPLE_ID).get().getAge();
        assertTrue(afterAge == initialAge + 1);

    }

}
