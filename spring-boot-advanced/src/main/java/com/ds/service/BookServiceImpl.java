package com.ds.service;

import com.ds.domain.Book;
import com.ds.domain.BookRepository;
import com.ds.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book getBookById(Long id){
        Book book = bookRepository.findOneById(id);

        if(book == null){
            throw new BookNotFoundException("データが存在しません");
        }

        return book;
    }
}
