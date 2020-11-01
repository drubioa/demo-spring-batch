package com.exaple.batch.demospringbatch.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="PEOPLE")
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    @Column(name="NAME", length=50)
    private String name;
    @Column(name="SURNAME", length=50)
    private String surname;
    @Column(name="AGE")
    private Integer age;

}
