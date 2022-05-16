package com.ds.service;

import com.ds.domain.Book;
import com.ds.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
public class BookService{

    @Autowired
    private BookRepository bookRepository;

    /**
     * 全書籍一覧を取得
     * @return
     */
    public List<Book> findAll(){

        return bookRepository.findAll();
        // aaa

    }

    /**
     * ページング
     * @return
     */
    public Page<Book> findAllByPage(Pageable pageable){

        return bookRepository.findAll(pageable);
    }

    /**
     * 本を新規作成or更新
     * @param book
     * @return
     */
    public Book sava(Book book){
        return bookRepository.save(book);
    }

    /**
     * 本を１冊取得
     * @param id
     * @return
     */
    public Book getOne(long id){

        return bookRepository.getOne(id);
    }

    public Book findOneById(long id) {
        return bookRepository.findById(id);
    }

    /**
     * 本を１冊削除
     * @param id
     */
    public void deleteOne(long id){
        bookRepository.deleteById(id);
    }

    /**
     * 作者検索
     * @param author
     * @return
     */
    public List<Book> findByAuthor(String author){

        return bookRepository.findByAuthor(author);
    }

    /**
     * 作者と状態で検索
     * @param author
     * @param status
     * @return
     */
    public List<Book> findByAuthorAndStatus(String author,int status){

        return bookRepository.findByAuthorAndStatus(author,status);

    }


    /**
     * 説明のキーワード検索
     * @param des
     * @return
     */
    public List<Book> findByDescriptionContains(String des){
        return bookRepository.findByDescriptionContains(des);
    }

    /**
     * SQL検索
     * @param len
     * @return
     */
    public List<Book> findByJPQL(int len){
        return bookRepository.findByJPQL(len);
    }

    /**
     * スタータスのみ更新
     * @param status
     * @param id
     * @return
     */
    public int updateByJPQL(int status,long id){
        return bookRepository.updateByJPQL(status,id);
    }

    /**
     * idからJPQLで削除
     * @param id
     * @return
     */
    @Transactional
    public int deleteByJPQL(long id){

        return bookRepository.deleteByJPQL(id);
    }

    /**
     * トランザクション処理
     * @param status
     * @param delId
     * @param upId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteAndUpdate(long delId,
                               int status,
                               long upId) throws Exception{

        try{
            int dcount = bookRepository.deleteByJPQL(delId);
            int upcount = bookRepository.updateByJPQL(status,upId);

            return dcount+upcount;

        }catch (Exception ex){
            throw new Exception(ex);

        }

    }
}
