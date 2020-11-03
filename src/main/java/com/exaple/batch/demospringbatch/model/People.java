package com.exaple.batch.demospringbatch.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name="people")
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    @Column(name="name", length=50)
    private String name;
    @Column(name="surname", length=50)
    private String surname;
    @Column(name="age")
    private Integer age;
    @OneToMany(mappedBy="people")
    private Set<Book> books;
}
