package com.example.demo.controller;

import com.example.demo.Model.Book;
import com.example.demo.service.Bookservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/books")
public class Bookcontroller {

    @Autowired
    private Bookservice bookservice;

    @GetMapping
        public List<Book> getAllBooks() {
        return bookservice.getAllBooks();
        }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Book book = bookservice.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookservice.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook( @PathVariable Integer id  ,  @Valid  @RequestBody  Book bookDetails) {
         Book updateBook = bookservice.updateBook(id , bookDetails);
         return ResponseEntity.ok(updateBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook( @PathVariable Integer id ) {
        bookservice.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
