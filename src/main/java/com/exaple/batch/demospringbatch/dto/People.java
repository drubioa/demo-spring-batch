package com.exaple.batch.demospringbatch.dto;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="PEOPLE")
public class People {

    @Id
    @Column(name = "person_id")
    private Long id;
    @Column(name="NAME", length=50)
    private String name;
    @Column(name="SURNAME", length=50)
    private String surname;

}
