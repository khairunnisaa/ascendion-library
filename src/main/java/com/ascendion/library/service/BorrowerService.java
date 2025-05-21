package com.ascendion.library.service;

import com.ascendion.library.entity.Borrower;
import com.ascendion.library.exception.ResourceAlreadyExistsException;
import com.ascendion.library.exception.ResourceNotFoundException;
import com.ascendion.library.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;

    public Borrower registerBorrower(Borrower borrower) {
        if (borrowerRepository.findByEmail(borrower.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Borrower with this email already exists");
        }
        return borrowerRepository.save(borrower);
    }

    public Borrower getById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }
}
