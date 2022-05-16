package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 一覧取得
     * @param model
     * @return
     */
    @GetMapping("/books")
    public String list(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        Page<Book> page1 = bookService.findAllByPage(pageable);
        model.addAttribute("page",page1);
        return "books";
    }

    /**
     * １件参照
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/books/{id}")
    public String detail(@PathVariable long id,
                         Model model) {

        Book book = bookService.findOneById(id);

        if (book == null) {
            book = new Book();
        }

        model.addAttribute("book", book);

        return "book";
    }

    /**
     * 新規作成 Get
     * @return
     */
    @GetMapping("/books/input")
    public String inputPage(Model model){
        model.addAttribute("book",new Book());
        return "input";

    }

    /**
     * 更新 Post
     * @param book
     * @return
     */
    @PostMapping("/books")
    public String post(Book book, final RedirectAttributes attributes){

        Book book1 = bookService.sava(book);

        if (book1 != null){
            attributes.addFlashAttribute("message","《"+book1.getName()+"》 が更新されました");
        }
        return "redirect:/books";

    }

    /**
     * 編集 Get
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/books/{id}/input")
    public String inputEditPage(@PathVariable long id, Model model){
        Book book = bookService.findOneById(id);
        model.addAttribute("book", book);
        return "input";
    }

    @GetMapping("/books/{id}/delete")
    public String delete(@PathVariable long id,
                         final RedirectAttributes attributes){

        bookService.deleteOne(id);
        attributes.addFlashAttribute("message","削除しました");
        return "redirect:/books";
    }

}
