package com.ascendion.library.service;

import com.ascendion.library.entity.Borrower;
import com.ascendion.library.exception.ResourceAlreadyExistsException;
import com.ascendion.library.exception.ResourceNotFoundException;
import com.ascendion.library.repository.BorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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

    @Test
    void getById_success() {
        Borrower borrower = new Borrower(1L, "John Doe", "john@example.com");
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));

        Borrower result = borrowerService.getById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(borrowerRepository, times(1)).findById(1L);
    }

    @Test
    void getById_borrowerNotFound_throwsException() {
        when(borrowerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            borrowerService.getById(1L);
        });
        verify(borrowerRepository, times(1)).findById(1L);
    }

    @Test
    void getAllBorrowers_success() {
        Borrower borrower1 = new Borrower(1L, "John Doe", "john@example.com");
        Borrower borrower2 = new Borrower(2L, "Jane Smith", "jane@example.com");
        when(borrowerRepository.findAll()).thenReturn(List.of(borrower1, borrower2));

        List<Borrower> borrowers = borrowerService.getAllBorrowers();

        assertNotNull(borrowers);
        assertEquals(2, borrowers.size());
        assertEquals("John Doe", borrowers.get(0).getName());
        assertEquals("Jane Smith", borrowers.get(1).getName());
        verify(borrowerRepository, times(1)).findAll();
    }
}
