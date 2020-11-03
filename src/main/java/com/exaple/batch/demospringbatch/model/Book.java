package com.exaple.batch.demospringbatch.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(name="title", length=50)
    private String title;
    @Column(name="ventas")
    private Integer ventas;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="person_id", nullable=false)
    private People people;
}
