package com.ds.web;

import com.ds.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2")
public class HelloController {



    public HelloController() {
    }

    @PostMapping("/say")
    public String hello(){
        return "Hello Spring Boot";
    }

    @GetMapping("/books")
            public Object getAll(@RequestParam("page") int page,
                         @RequestParam(value = "size",defaultValue = "10") int size){

        Map<String, Object> book = new HashMap<>();
        book.put("name","インターネットの世界観");
        book.put("isbn","93089275230");
        book.put("author","田中洋一");

        Map<String, Object> book2 = new HashMap<>();
        book2.put("name","良いコードとは");
        book2.put("isbn","297490213471");
        book2.put("author","岡田次郎");

        List<Map> contents = new ArrayList<>();
        contents.add(book);
        contents.add(book2);

        Map<String,Object> pagemap = new HashMap<>();
        pagemap.put("page",page);
        pagemap.put("size",size);
        pagemap.put("contents",contents);

        return pagemap;
    }

    /**
     * 正規表現: {パラメータ名:正規表現}
     * @param id
     * @param
     * @return
     */
    @GetMapping("/books/{id}")
    public Object getOne(@PathVariable long id){

        return null;
    }

    @PostMapping("/books")
    public Object post(@RequestParam("name") String name,
                       @RequestParam("author") String author,
                       @RequestParam("isbn") String isbn){
        Map<String, Object> book = new HashMap<String ,Object>();
        book.put("name",name);
        book.put("author",author);
        book.put("isbn",isbn);
        return book;
    }
}
