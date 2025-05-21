package com.ascendion.library;

import com.ascendion.library.entity.Borrower;
import com.ascendion.library.exception.ResourceAlreadyExistsException;
import com.ascendion.library.repository.BorrowerRepository;
import com.ascendion.library.service.BorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    private BorrowerRepository borrowerRepository;
    private BorrowerService borrowerService;

    @BeforeEach
    void setUp() {
        borrowerRepository = mock(BorrowerRepository.class);
        borrowerService = new BorrowerService(borrowerRepository);
    }

    @Test
    void registerBorrower_success() {
        Borrower borrower = new Borrower(null, "John Doe", "john@example.com");
        when(borrowerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(borrowerRepository.save(any())).thenReturn(borrower);

        Borrower result = borrowerService.registerBorrower(borrower);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(borrowerRepository, times(1)).save(borrower);
    }

    @Test
    void registerBorrower_duplicateEmail_throwsException() {
        Borrower borrower = new Borrower(null, "John Doe", "john@example.com");
        when(borrowerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(borrower));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            borrowerService.registerBorrower(borrower);
        });
        verify(borrowerRepository, never()).save(any());
    }
}
