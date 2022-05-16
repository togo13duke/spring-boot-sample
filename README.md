# spring-boot
spring bootの学習メモ

## 入門編

### spring bootプロジェクト作成

spring bootを作成するには３種類の方法が存在する

1. IDE : Eclipse, Intellij
2. [公式INITIALIZR](https://start.spring.io/) : Web上で作成
3. Spring Boot CLI

### Hello World

IDEでプロジェクト作成後にwebパッケージ作成する。
Controllerクラスを新規追加する。

```java
package com.ds.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// コントローラの注釈
@RestController
public class HelloController {

    // リクエスト注釈
    @RequestMapping("/say")
    public String hello(){
        return "Hello Spring Boot";
    }
}
```

### RESTful URL

クライアントのデバイスの多様化（PC,スマホ,iPad, etc）に伴い、サーバーサイド側の統一する設計が必要になった。

本章はWebApiを通して、API開発基礎を学習する。

今回作成するRESTful一覧

- **GET**    http://localhost:8080/api/vi/books    書籍一覧取得
- **POST**   http://localhost:8080/api/vi/books    １冊の本を新規作成
- **GET**    http://localhost:8080/api/vi/books/{id}  １冊の本を取得
- **PUT**    http://localhost:8080/api/vi/books 1冊の本を更新
- **DELETE** http://localhost:8080/api/vi/books/{id} 1冊の本を削除

クラス上に注釈を追加することで、そのクラスはWEBコントローラーになる。

```java
@RestController
@RequestMapping("/api")
public class HelloController {}
```

メソッド上に注釈を追加することで、そのメソッドはWEBリクエスト受付の機能ができる。

```java
@RequestMapping(value = "/say",method = RequestMethod.GET)
public String hello(){
    return "hello";
}

// GETリクエスト（下が省略の書き方）
@RequestMapping(value = "/say",method = RequestMethod.GET)
@GetMapping("/say")

// POSTリクエスト（下が省略の書き方）
@RequestMapping(value = "/say",method = RequestMethod.POST)
@PostMapping("/say")
```

[**PostMan**](https://www.getpostman.com/)を使えばget,postなどのリクエストを投げることができる。  
ただ、社内環境ではローカルホストへのリクエストがプロキシに阻まれてうまく接続できない。  
Chromeの拡張機能のPostManを使えば対応できる。（拡張機能は廃止予定だが...）

**追記**

Chromeの拡張機能[**Advanced REST client**](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo?hl=ja)は社内環境でも使える。


リクエストのパラメータ受け取り方法は２種類ある。

* PathVariable

```java
@GetMapping("/books/{id}/{username:[a-z_]+}")
public Object getOne(@PathVariable long id,
                     @PathVariable String username){}

```

* RequestParam

```java
@PostMapping("/books")
public Object post(@RequestParam("name") String name,
                   @RequestParam("author") String author,
                   @RequestParam("isbn") String isbn
                   // デフォルト値設定
                   @RequestParam(value = "size",defaultValue = "10") int size){
```

### プロパティファイル

resourcesの直下のapplication.propertiesファイルで設定を変更することができる。  
※resources/configに配置しても同じく認識する

```properties
server.port=8082
```

yml形式（application.yml）プロパティファイルにすることもできる。

```yml
server:
  port: 8082
  servlet:
    context-path: /api
logging:
  level:
    root: warn
    com.lrm: debug
  file: logs/my.log
```

#### 自己定義プロパティ

自分で定義した値もプロパティファイルで利用可能。

```yml
book:
  name: インターネットの世界観
  author: 田中洋一
  isbn: ${random.uuid}
  desctiption: ${book.name},この本は悪くない
```

```java
@Value("${book.name}")
private String name;

@Value("${book.author}")
private String author;

@Value("${book.isbn}")
private String isbn;

@Value("${book.desctiption}")
private String desctiption;
```

定義した値をインスタンスに注入する

```java
@Component
@ConfigurationProperties(prefix = "book")
public class Book {

    private String name;

    private String author;

    private String isbn;

    private String desctiption;

    public Book(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }
}
```

```java
public class HelloController {

    @Autowired
    private Book book;

    @GetMapping("/books/{id}")
    public Object getOne(@PathVariable long id){

        return book;
    }
```

#### 環境ごとプロパティファイル

環境ごとのプロパティファイルを定義する場合、以下の命名規則で環境ごとのファイルを作成する。

* 命名規則  
application-{profile}.yml

例

```yml:application.yml
spring:
  profiles:
    active: dev

book:
  name: インターネットの世界観
  author: 田中洋一
  isbn: ${random.uuid}
  desctiption: ${book.name},この本は悪くない
```

```yml:application-dev.yml
server:
  port: 8080
  #Sservlet:
    #context-path: /api
logging:
  level:
    root: debug
    com.ds: debug
  file: logs/debug.log
```

### JPA

JPA(Java Prosistence API)は関係データベースのデータを扱うアプリケーションを開発するためのJava用フレームワークである。

Spring Data JPAは、Java Persistence API（JPA）のリポジトリサポートを提供する。  
JPAデータソースにアクセスする必要があるアプリケーションの開発を容易にする。

#### JPA初期設定

Maven（pom.xml）の参照を追加

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

プロパティファイル

```yml

spring:
  datasource:
    # Xampp使っているので、MariaDBのドライバー
    data-username: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/book
    username: root
    password: password
    connectionProperties: useUnicode=true&characterEncoding=UTF-8&serverTimezone=JST

  jpa:
    hibernate:
      # ddl-autoをcreateに設定すると、APP起動するたび既存のデータが削除される。updateが一般的
      ddl-auto: update 
    # SQL文の出力
    show-sql: true
    properties:
      hibernate:
        # MariaDB
        dialect: org.hibernate.dialect.MariaDBDialect
```

Entityクラス

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // アノテーションEntityを注入
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //これを付けないとJsonErrorになる…
public class Book {

    @Id // 主キー
    @GeneratedValue(strategy = GenerationType.AUTO) // 生成方法:自動
    private long id;

    private String name;

    public Book(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```


#### JPA基本操作

エンティティのリポジトリのインターフェースを作成

```java
package com.ds.domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

}
```

リポジトリを呼び出すサービスクラスを作成

```java
package com.ds.service;

import com.ds.domain.Book;
import com.ds.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService{

    // リポジトリ
    @Autowired
    private BookRepository bookRepository;

    /**
     * 一覧取得
     * @return
     */
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    /**
     * 新規作成or更新
     * @param book
     * @return
     */
    public Book sava(Book book){
        return bookRepository.save(book);
    }

    /**
     * 1件取得
     * @param id
     * @return
     */
    public Book getOne(long id){
        return bookRepository.getOne(id);
    }

    /**
     * 本を１冊削除
     * @param id
     */
    public void deleteOne(long id){
        bookRepository.deleteById(id);
    }
}
```

リクエストを受け付けるコントローラーを作成

```java
package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookApp {

    // サービス
    @Autowired
    private BookService bookService;

    /**
     * 書籍一覧を取得
     * @return
     */
    @GetMapping("/books")
    public List<Book> getAll(){
        return bookService.findAll();
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
     * 本を1件を取得
     * @param id
     * @return
     */
    @GetMapping("/books/{id}")
    public Book getOne(@PathVariable long id){
        return bookService.getOne(id);
    }

    /**
     * 本1件を更新
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
        book.setDesctiption(description);
        book.setStatus(status);
        return bookService.sava(book);
    }

    /**
     * 本1件削除処理
     * @param id
     */
    @DeleteMapping("/books/{id}")
    public void deleteOne(@PathVariable long id){
        bookService.deleteOne(id);
    }
}
```

**注意点**

**POST**メソッドのリクエストBodyのパラメータ形式は**form-data**で問題ないが、**PUT**メソッドのリクエストBodyのパラメータ形式は**x-www-form-urlencoded**にする必要がある。

実業務ではリクエストはGETとPOSTだけ使われることが多い。


#### JPAの複雑な検索方法

書籍の作者名から検索する場合、Repositoryで定義する。

Repositoryで検索メソッドを定義する。

定義方法下記の公式から参照すること。

[**spring data jpa**](https://docs.spring.io/spring-data/jpa/docs/2.1.4.RELEASE/reference/html/#jpa.repositories)

```java
package com.ds.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    // 自己定義したメソッド名で自動的にSQLが生成される
     List<Book> findByAuthor(String author);
     List<Book> findByAuthorAndStatus(String author,int status);
     List<Book> findByDescriptionContains(String des);
}
```

Setvice

```java
public List<Book> findByAuthor(String author){
    return bookRepository.findByAuthor(author);
}

public List<Book> findByAuthorAndStatus(String author,int status){
    return bookRepository.findByAuthorAndStatus(author,status);
}

public List<Book> findByDescriptionContains(String des){
    return bookRepository.findByDescriptionContains(des);
}
```

#### JPAのSQL検索方法

直接SQLでデータを検索する場合もRepositoryで検索メソッドを定義する。

```java
// JPQL
@Query("select b from Book b where length(b.name) > ?1 and b.status = ?2") 
List<Book> findByJPQL(int len, int status);

//生のSQL
@Query(value = "select * from book where LENGTH(name) > ?1",nativeQuery = true) 
List<Book> findByJPQL2(int len);
```

#### JPAのSQL更新・削除

既存のJPAの更新メソッド（sava）を用いたときにデータ項目は全て更新される。  
一部の項目のみ更新したい場合はJPQLを使用することで実現できる。

戻り値のintは更新or削除した件数

```java
@Transactional // 更新のトランザクションのアノテーションが必要
@Modifying // 更新系に必要
@Query("update Book b set b.status = ?1 where b.id = ?2")
int updateByJPQL(int status,long id);

@Transactional
@Modifying
@Query("delete from Book b where b.id = ?1")
int deleteByJPQL(long id);
```

#### JPAのトランザクション処理

Service側でもトランザクション処理を定義できる。  
@Transactional既定の設定は「実行時例外 RuntimeException」のみRollbackされる。
検査例外のときはRollbackされない。

```java
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
```

### Thymeleaf

Thymeleafとはspring frameworkのHTMLのテンプレートエンジン。  
純粋なHTMLとしても記述でき、クライアントとサーバー側の分離が可能。  
デザイナーとプログラマが同じHTMLを共有して使用できる。

#### Thymeleafの使用方法

Mavenで依存関係を追加(pom.xml)

Spring Bootのバージョンが2以降であれば、thymeleafはバージョン3になるので特に意識する必要はない。  
Spring Bootのバージョンが1であれば、thymeleafはバージョン2が初期値。バージョン3を使うには公式サイトを参考すること。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

コントローラでthymeleafを使用

```java
@Controller // Controllerのアノテーションを注入
public class BookController {

    @GetMapping("/books")
    public String list() {
        return "books"; // 対象のthymeleafの名前をここで指定する
    }
}
```

src\maim\resources\templatesの配下にthymeleafを配置  
ファイル名は**コントローラで指定した名前**。ここではbooks.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" >
    <title>books</title>
</head>

<body>
    <h1>Books List</h1>
</body>
</html>
```

#### Thymeleafのデータ取得

コントローラでデータ取得、モデルバインディングを行う

```java
@GetMapping("/books/{id}")
public String detail(@PathVariable long id,
                        Model model) { // Modelを宣言

    Book book = bookService.getOne(id);

    if (book == null) {
        book = new Book();
    }

    model.addAttribute("book", book); // 取得したBookオブジェクトをmodelにバインディングする

    return "book";
}
```

thymeleafで対応したモデルを表示させる。

**th:** 付いているタグに注目

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Book詳細</title>
</head>
<body>

<!-- 直接modelを指定するパターン --> 
<div>
    <p>
        <strong>書籍名: </strong><span th:text="${book.name}">リーダブルコード</span>
    </p>
    <p>
        <strong>作者: </strong><span th:text="${book.author}">Tom</span>
    </p>
    <p>
        <strong>説明: </strong><span th:text="${book.description}">プログラマ必ず読むべき</span>
    </p>
    <p>
        <strong>状態: </strong><span th:text="${book.status}">0</span>
    </p>
</div>

<!-- 上位のタグでmodelを指定するパターン model多いとこっちのほうが書き方が楽 --> 
<div  th:object="${book}">
    <p>
        <strong>書籍名: </strong><span th:text="*{name}">リーダブルコード</span>
    </p>
    <p>
        <strong>作者: </strong><span th:text="*{author}">Tom</span>
    </p>
    <p>
        <strong>説明: </strong><span th:text="*{description}">プログラマ必ず読むべき</span>
    </p>
    <p>
        <strong>状態: </strong><span th:text="*{status}">0</span>
    </p>
</div>

</body>
</html>
```

#### 静的コンテンツ

静的コンテンツ（boostrap,jquery）はsrc\maim\resources\static配下に配置する

thymeleafでの適応は**th:** を使う

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <title>Book詳細</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}" href="../static/css/bootstrap.min.css">

</head>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script th:src="@{/js/jquery-3.3.1.min.js}" src="../static/js/jquery-3.3.1.min.js"></script>
<script th:src="@{/js/bootstrap.min.js}" src="../static/js/bootstrap.min.js"></script>
</body>
</html>
```

### Thymeleaf(分岐、反復、URL)

```html
<!-- swith文 -->
<p th:switch="*{status}">
    <strong>状態: </strong>
    <span th:case="0">未読</span>
    <span th:case="1">読書中</span>
    <span th:case="2">完読</span>
</p>

<!-- if文 -->
<div class="alert alert-warning" th:if="*{status == 0}">
    <strong>注意</strong>
</div>

<div class="alert alert-success"th:unless="*{status == 0}">
    <strong>ナイス</strong>
</div>

<!-- for文 -->

<!-- iterStat 項目取得
count : 数 1から
index : インデックス 0から
size : 総件数
even/odd : 奇数偶数
first/last(bool) : 1件目,最終件
-->

<tr th:each="book,iterStat:${books}">
    <td th:text="${iterStat.count}">1</td>
    <td th:text="${book.name}">書籍名</td>
    <td th:text="${book.author}">作者</td>
    <td th:text="${book.description}">説明</td>

    <td th:switch="${book.status}">
        <span th:case="0" class="text-danger">未読</span>
        <span th:case="1">読書中</span>
        <span th:case="2" class="text-success">完読</span>
        <span th:case="*">状態</span>
    </td>
</tr>

<!-- URL -->
<a href="#" th:text="${book.name}" th:href="@{'books/' + ${book.id}}">書籍名</a>
```