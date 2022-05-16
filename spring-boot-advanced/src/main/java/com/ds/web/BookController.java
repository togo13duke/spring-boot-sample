package com.ds.web;

import com.ds.domain.Book;
import com.ds.exception.BookNotFoundException;
import com.ds.service.BookService;
import com.ds.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public String getBook(@PathVariable Long id, Model model){

        Book book = bookService.getBookById(id);
        model.addAttribute("book",book);
        return "book";
    }
}
