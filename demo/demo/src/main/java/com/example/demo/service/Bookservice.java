package com.example.demo.service;

import com.example.demo.Exception.BookNotFoundException;
import com.example.demo.Model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class Bookservice {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Integer id , Book bookDetails) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found id " + id ));
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setDescription(bookDetails.getDescription());
        existingBook.setPrice(bookDetails.getPrice());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found id " +id));
        bookRepository.delete(book);
    }
}
