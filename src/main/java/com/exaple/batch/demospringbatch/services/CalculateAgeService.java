package com.exaple.batch.demospringbatch.services;

import com.exaple.batch.demospringbatch.model.People;
import com.exaple.batch.demospringbatch.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class CalculateAgeService {

    @Autowired
    private PeopleRepository peopleRepository;

    public void recalculateAge(final People p) {
        p.setAge(p.getAge() + 1);
        peopleRepository.save(p);
    }

}
