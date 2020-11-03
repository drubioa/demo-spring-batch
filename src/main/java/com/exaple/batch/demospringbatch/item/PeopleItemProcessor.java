package com.exaple.batch.demospringbatch.item;

import com.exaple.batch.demospringbatch.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PeopleItemProcessor implements ItemProcessor<Book, Book> {

    @Override
    public Book process(Book book) throws Exception {
        log.info("Incrementando ventas de libros y edad de autor");
        book.setVentas(book.getVentas() != null ? book.getVentas() + 1 : 0 );
        book.getPeople().setAge(book.getPeople().getAge() + 1);
        return book;
    }
}
