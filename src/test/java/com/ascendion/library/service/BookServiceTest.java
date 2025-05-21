package com.ascendion.library;

import com.ascendion.library.entity.Book;
import com.ascendion.library.entity.Borrower;
import com.ascendion.library.exception.BookAlreadyBorrowedException;
import com.ascendion.library.exception.ResourceNotFoundException;
import com.ascendion.library.repository.BookRepository;
import com.ascendion.library.service.BookService;
import com.ascendion.library.service.BorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BorrowerService borrowerService;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        borrowerService = mock(BorrowerService.class);
        bookService = new BookService(bookRepository, borrowerService);
    }

    @Test
    void registerBook_validBook_savesBook() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Clean Code")
                .author("Robert C. Martin")
                .build();

        when(bookRepository.findByIsbn("1234567890")).thenReturn(List.of());
        when(bookRepository.save(book)).thenReturn(book);

        Book saved = bookService.registerBook(book);

        assertEquals("Clean Code", saved.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void registerBook_sameIsbnDifferentTitle_throwsException() {
        Book existingBook = Book.builder()
                .isbn("1234567890")
                .title("Clean Code")
                .author("Robert C. Martin")
                .build();

        Book newBook = Book.builder()
                .isbn("1234567890")
                .title("Different Title")
                .author("Robert C. Martin")
                .build();

        when(bookRepository.findByIsbn("1234567890")).thenReturn(List.of(existingBook));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookService.registerBook(newBook);
        });

        assertTrue(thrown.getMessage().contains("Books with same ISBN must have same title and author"));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void borrowBook_bookAvailable_borrowsSuccessfully() {
        Book book = Book.builder().id(1L).borrowedBy(null).build();
        Borrower borrower = Borrower.builder().id(1L).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowerService.getById(1L)).thenReturn(borrower);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> bookService.borrowBook(1L, 1L));

        assertEquals(borrower, book.getBorrowedBy());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void borrowBook_bookAlreadyBorrowed_throwsException() {
        Borrower otherBorrower = Borrower.builder().id(2L).build();
        Book book = Book.builder().id(1L).borrowedBy(otherBorrower).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookAlreadyBorrowedException thrown = assertThrows(BookAlreadyBorrowedException.class, () -> {
            bookService.borrowBook(1L, 1L);
        });

        assertTrue(thrown.getMessage().contains("Book is already borrowed"));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void returnBook_validReturn_succeeds() {
        Borrower borrower = Borrower.builder().id(1L).build();
        Book book = Book.builder().id(1L).borrowedBy(borrower).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> bookService.returnBook(1L, 1L));
        assertNull(book.getBorrowedBy());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void returnBook_notBorrowedByThisBorrower_throwsException() {
        Borrower borrower = Borrower.builder().id(1L).build();
        Borrower differentBorrower = Borrower.builder().id(2L).build();
        Book book = Book.builder().id(1L).borrowedBy(differentBorrower).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            bookService.returnBook(1L, 1L);
        });

        assertTrue(thrown.getMessage().contains("This borrower did not borrow this book"));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void borrowBook_bookNotFound_throwsResourceNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.borrowBook(1L, 1L);
        });
    }

    @Test
    void returnBook_bookNotFound_throwsResourceNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.returnBook(1L, 1L);
        });
    }
}
