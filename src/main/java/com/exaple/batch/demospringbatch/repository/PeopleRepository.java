package com.exaple.batch.demospringbatch.repository;

import com.exaple.batch.demospringbatch.model.People;
import org.springframework.data.repository.CrudRepository;

public interface PeopleRepository extends CrudRepository<People, Long> {

}
