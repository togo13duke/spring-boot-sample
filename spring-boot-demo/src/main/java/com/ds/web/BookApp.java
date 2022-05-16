package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookApp {

    @Autowired
    private BookService bookService;

    /**
     * 書籍一覧を取得
     * @return
     */
    @GetMapping("/books")
    public Page<Book> getAll(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "1") int size){

        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return bookService.findAllByPage(PageRequest.of(page,size,sort));
    }

    /**
     * 新規作成
     * @return
     */
    @PostMapping("/books")
    public Book post(Book book){

//        Book book = new Book();
//        book.setName(name);
//        book.setAuthor(author);
//        book.setDesctiption(description);
//        book.setStatus(status);

        return bookService.sava(book);
    }


    /**
     * 1件の本を取得
     * @param id
     * @return
     */
    @GetMapping("/books/{id}")
    public Book getOne(@PathVariable long id){

        return bookService.getOne(id);

    }

    /**
     * 更新処理
     * @param id
     * @param name
     * @param author
     * @param description
     * @param status
     * @return
     */
    @PutMapping("/books")
    public Book update(@RequestParam long id,
                       @RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String description,
                       @RequestParam int status){

        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        book.setStatus(status);

        return bookService.sava(book);

    }

    /**
     * 本を１冊削除
     * @param id
     */
    @DeleteMapping("/books/{id}")
    public void deleteOne(@PathVariable long id){
        bookService.deleteOne(id);
    }



    @PostMapping("/books/by")
    public int findBy(
            //@RequestParam String author,
            //@RequestParam int status,
            //@RequestParam String des
            //@RequestParam int len
            @RequestParam long delId,
            @RequestParam int status,
            @RequestParam long upId
    ){

        try{
            return bookService.deleteAndUpdate(delId,status,upId);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return 0;

        //return bookService.deleteAndUpdate(delId,status,upId);
        //return bookService.deleteByJPQL(id);
        //return bookService.updateByJPQL(status,id);
        //return bookService.findByJPQL(len);
        //return bookService.findByDescriptionContains(des);
        //return bookService.findByAuthorAndStatus(author,status);
        //return bookService.findByAuthor(author);

    }


}
