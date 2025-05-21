package com.ascendion.library.controller;

import com.ascendion.library.entity.Book;
import com.ascendion.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book registerBook(@Valid @RequestBody Book book) {
        return bookService.registerBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/{bookId}/borrow/{borrowerId}")
    public void borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        bookService.borrowBook(bookId, borrowerId);
    }

    @PostMapping("/{bookId}/return/{borrowerId}")
    public void returnBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        bookService.returnBook(bookId, borrowerId);
    }
}
