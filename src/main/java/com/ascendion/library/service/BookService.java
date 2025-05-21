package com.ascendion.library.service;

import com.ascendion.library.entity.Book;
import com.ascendion.library.entity.Borrower;
import com.ascendion.library.exception.BookAlreadyBorrowedException;
import com.ascendion.library.exception.BookNotAvailableException;
import com.ascendion.library.exception.ResourceNotFoundException;
import com.ascendion.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BorrowerService borrowerService;

    public Book registerBook(Book book) {
        List<Book> booksWithSameIsbn = bookRepository.findByIsbn(book.getIsbn());
        for (Book b : booksWithSameIsbn) {
            if (!b.getTitle().equals(book.getTitle()) || !b.getAuthor().equals(book.getAuthor())) {
                throw new IllegalArgumentException("Books with same ISBN must have same title and author");
            }
        }
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }
        Borrower borrower = borrowerService.getById(borrowerId);
        book.setBorrowedBy(borrower);
        bookRepository.save(book);
    }

    public void returnBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (book.isAvailable() || !book.getBorrowedBy().getId().equals(borrowerId)) {
            throw new BookNotAvailableException("This borrower did not borrow this book");
        }
        book.setBorrowedBy(null);
        bookRepository.save(book);
    }
}
